package main.java;

import main.java.Comparators.ComparatorNormalEvent;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.Event.Query;
import main.java.Event.QueryType;
import main.java.Interface.InterfaceController;
import main.java.Modules.*;
import main.java.Statistics.SimulationStatistics;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulator {
    //Constants
    public static final int M_CLIENTS = 0; 
    public static final int M_PROCESSES = 1; 
    public static final int M_QUERIES = 2; 
    public static final int M_TRANSACTIONS = 3; 
    public static final int M_EXECUTIONS = 4;


    private RandomValueGenerator valueGenerator;
    private ClientCommunicationsManagerModule clientCommunicationsManagerModule;
    private ProcessManagerModule processManagerModule;
    private QueryProcessorModule queryProcessorModule;
    private TransactionalStorageModule transactionalStorageModule;
    private ExecutorModule executorModule;
    private PriorityQueue<Event> queue;

    private double maxSimulationTime;
    private boolean delay;
    private int maxConcurrentConnectionsSystem;
    private int maxConcurrentConnectionsQueries;
    private int maxQueriesInTransactions;
    private int maxQueriesInExecutor;
    private double timeout;
    private double clockTime;
    private boolean firstEvent;
    private boolean stopSimulation;

    private SimulationStatistics simulationStatistics;
    private InterfaceController interfaceController;

    private int timeoutNumber;

    /**
     * class constructor
     * @param interfaceController Interface where the real-time simulation data and statistics are displayed.
     */
    public Simulator(InterfaceController interfaceController){
        this.interfaceController = interfaceController;
    }

    /**
     * Method that initializes the simulation variables according to the parameters.
     * @param parameters Simulation parameters indicated by the user.
     */
    public void setParameters(Object parameters[]){
        maxSimulationTime = (Double) parameters[1];
        delay = (Boolean) parameters[2];
        maxConcurrentConnectionsSystem = (Integer) parameters[3];
        maxConcurrentConnectionsQueries = (Integer) parameters[4];
        maxQueriesInTransactions = (Integer) parameters[5];
        maxQueriesInExecutor = (Integer) parameters[6];
        timeout = (Double) parameters[7];
        stopSimulation = (Boolean)parameters[8];
    }

    /**
     * Method that initializes the rest of the variables (modules, statistics and others), based on the user parameters.
     */
    public void initialize(){
        this.valueGenerator = new RandomValueGenerator();
        this.queue = new PriorityQueue<>(new ComparatorNormalEvent());
        this.firstEvent = true;
        this.clockTime = 0;
        this.timeoutNumber = 0;

        clientCommunicationsManagerModule =
                new ClientCommunicationsManagerModule(this, valueGenerator, this.maxConcurrentConnectionsSystem);

        processManagerModule =
                new ProcessManagerModule(this, valueGenerator);

        queryProcessorModule =
                new QueryProcessorModule(this, valueGenerator , this.maxConcurrentConnectionsQueries);

        transactionalStorageModule =
                new TransactionalStorageModule(this, valueGenerator, this.maxQueriesInTransactions);

        executorModule =
                new ExecutorModule(this, valueGenerator, this.maxQueriesInExecutor);


        simulationStatistics = new SimulationStatistics(clientCommunicationsManagerModule.getStatisticsOfModule(),
                                                        processManagerModule.getStatisticsOfModule(),
                                                        queryProcessorModule.getStatisticsOfModule(),
                                                        transactionalStorageModule.getStatisticsOfModule(),
                                                        executorModule.getStatisticsOfModule());

        if(stopSimulation){
            this.interfaceController.hideNextButton(true);
            this.interfaceController.showStatsButton(false);
        }

    }

    /**
     * returns true if the simulation has to be stopped
     * @return boolean with de values
     */
    public boolean getStopSimulation(){
        return stopSimulation;
    }

    /**
     * Method that creates a new event (arrival to the connections module), assigns it an arrival
     * time with exponential distribution
     * (first arrival is always time 0) and then inserts it into the list of events of the simulation.
     */
    public void generateNewEvent(){

        //Create query for the event
        Query query = generateQuery();

        double timeTemp = 0;
        if(!firstEvent){
            //30 connections per minute -> 1/lambda = 30 connections/min -> 1/lambda = 0.5connections/sec -> lambda = 2
            timeTemp = valueGenerator.generateExponentialDistributionValue(0.5 );
        }else {
            firstEvent = false;
        }

        //Add into the list of events
        queue.offer(new Event(query, clockTime+timeTemp, EventType.ARRIVAL, clientCommunicationsManagerModule));
    }

    /**
     * Method that creates, using the Monte Carlo method, the query for the event.
     * @return Query created.
     */
    public Query generateQuery(){
        Random randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
        Query query;
        if (random < 0.30){
            query = new Query(QueryType.SELECT);
        } else if (random < 0.55){
            query = new Query(QueryType.UPDATE);
        } else if (random < 0.90){
            query = new Query(QueryType.JOIN);
        } else {
            query = new Query(QueryType.DDL);
        }
        return query;
    }

    /**
     * Method that the modules use to insert events in the list of events of the simulation.
     * Verify if the event to be inserted is timeout (not inserted and and updates statistics) or not (if it is inserted).
     * @param event Event to be inserted into the list.
     */
    public void addEvent(Event event){
        if(!isTimeOut(event)){
            queue.offer(event);
        }else{
            event.getCurrentModule().processTimeoutEvent(event, false);
        }
    }

    /**
     * Method that sends the queue of each module to be reviewed, to eliminate the timeout events that may be waiting.
     */
    public void checkTimeOutSystemQueues(){
        checkTimeOutQueue(this.processManagerModule.getQueue());
        checkTimeOutQueue(this.queryProcessorModule.getQueue());
        checkTimeOutQueue(this.transactionalStorageModule.getQueue());
        checkTimeOutQueue(this.executorModule.getQueue());
    }

    /**
     * Method that runs through the module's queue (indicated in parameter) and eliminates events that have timeout,
     * also updates statistics data.
     * @param queueR Queue of the module to review.
     */
    public void checkTimeOutQueue(PriorityQueue<Event> queueR){
        queueR.removeIf(this::isTimeOut);
    }

    /**
     * Method that checks if the event is timeout (takes a long time in the system).
     * @param event Event to review.
     * @return Boolean indicating whether it is timeout or not.
     */
    public boolean isTimeOut(Event event){
        if((event.getTimeClock() - event.getQuery().getQueryStatistics().getArrivalTime())>this.timeout){
            ++this.timeoutNumber;
            if(event.getCurrentModule()!= this.clientCommunicationsManagerModule)
                this.clientCommunicationsManagerModule.decreaseBusyServer();
            return true;
        }else{
            return false;
        }
    }


    /**
     * Method that controls a simulation.
     * It is responsible for creating the initial event and entering a simulation cycle until the time indicated by
     * the user ends.
     * The events are removed and processed from the list and for each event the modified data is shown in the interface
     * (in real time).
     * It also modifies the time of the simulation clock and places a delay in the interface if the user requested it.
     */
    public void simulate(){
        generateNewEvent();
        while (this.clockTime <= this.maxSimulationTime){
            Event event = queue.poll();
            checkTimeOutSystemQueues();
            if(event!=null){
                this.clockTime = event.getTimeClock();
                if(this.clockTime <= this.maxSimulationTime)
                    event.getCurrentModule().processEvent(event);
            }else{
                clockTime = maxSimulationTime+1;
            }
            if(delay)
                this.updateData();
            delay();
        }

        this.simulationStatistics.setTimeoutQueries(timeoutNumber);

        if (!delay)
            this.updateData();
    }


    /**
     * Method that updates the sizes of the queues in each module.
     * @return Array with the queue sizes in the modules.
     */
    public int[] getModulesQueueLength(){
        int queueLength[] = new int[5];
        queueLength[M_CLIENTS] = this.clientCommunicationsManagerModule.getQueue().size();
        queueLength[M_PROCESSES] = this.processManagerModule.getQueue().size();
        queueLength[M_QUERIES] = this.queryProcessorModule.getQueue().size();
        queueLength[M_TRANSACTIONS] = this.transactionalStorageModule.getQueue().size();
        queueLength[M_EXECUTIONS] = this.executorModule.getQueue().size();
        return queueLength;
    }

    /**
     * Method that updates the number of DDL queries processed by the modules.
     * @return Array with the amount of DDL processed in each module.
     */
    public int[] getDDLNumber(){
        int DDLNumber[] = new int[5];
        DDLNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfDDL();
        return DDLNumber;
    }

    /**
     * Method that updates the number of Update queries processed by the modules.
     * @return Array with the amount of Update processed in each module.
     */
    public int[] getUpdateNumber(){
        int updateNumber[] = new int[5];
        updateNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfUPDATE();
        return updateNumber;
    }

    /**
     * Method that updates the number of Join queries processed by the modules.
     * @return Array with the amount of Join processed in each module.
     */
    public int[] getJoinNumber(){
        int joinNumber[] = new int[5];
        joinNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfJOIN();
        return joinNumber;
    }

    /**
     * Method that updates the number of Select queries processed by the modules.
     * @return Array with the amount of Select processed in each module.
     */
    public int[] getSelectNumber(){
        int selectNumber[] = new int[5];
        selectNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfSELECT();
        return selectNumber;
    }

    /**
     * Method that updates the data in the interface in real time, requests the updated data of each module and is sent
     * to the interface to be displayed.
     */
    public void updateData(){
        this.interfaceController.updateClock(this.clockTime);
        this.interfaceController.updateDiscarded(this.simulationStatistics.getDiscardedNumberOfQueries());
        this.interfaceController.showDDLNumber(this.getDDLNumber());
        this.interfaceController.showJoinNumber(this.getJoinNumber());
        this.interfaceController.showSelectNumber(this.getSelectNumber());
        this.interfaceController.showUpdateNumber(this.getUpdateNumber());
        this.interfaceController.showQueueLength(this.getModulesQueueLength());
        this.interfaceController.updateTimeoutNumber(timeoutNumber);
    }

    /**
     * Method that makes a delay (if requested), so that the user can observe the change of the data throughout the
     * simulation.
     */
    public void delay(){
        if(delay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the queue average sizes in the modules.
     */
    public double[] getAverageModulesQueueLength(){
        double queueLength[] = new double[5];
        queueLength[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageSizeQueue();
        queueLength[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageSizeQueue();
        queueLength[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageSizeQueue();
        queueLength[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageSizeQueue();
        queueLength[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageSizeQueue();
        return queueLength;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getAverageTimeDDL(){
        double DDLNumber[] = new double[5];
        DDLNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        return DDLNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getAverageTimeUpdate(){
        double updateNumber[] = new double[5];
        updateNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        return updateNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getAverageTimeJoin(){
        double joinNumber[] = new double[5];
        joinNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        return joinNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getAverageTimeSelect(){
        double selectNumber[] = new double[5];
        selectNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        return selectNumber;
    }

    /**
     * Method that shows the average of the data of each run.
     */
    public void updateAverageData(){
        this.interfaceController.showTotalQueries(this.simulationStatistics.getNumberOfArrivals());
        this.interfaceController.showAverageTimelifeQuery(this.simulationStatistics.getTimeLifeOfQuery());
        this.interfaceController.showDDLAverageTime(this.getAverageTimeDDL());
        this.interfaceController.showJoinAverageTime(this.getAverageTimeJoin());
        this.interfaceController.showSelectAverageTime(this.getAverageTimeSelect());
        this.interfaceController.showUpdateAverageTime(this.getAverageTimeUpdate());
        this.interfaceController.showQueueAverageLength(this.getAverageModulesQueueLength());
        this.interfaceController.updateTimeoutNumber(timeoutNumber);
    }
    /**
     * Method to access the simulation variable clientCommunicationsManagerModule.
     * @return Module of simulation clientCommunicationsManagerModule
     */
    public ClientCommunicationsManagerModule getClientCommunicationsManagerModule() {
        return clientCommunicationsManagerModule;
    }

    /**
     * Method to access the simulation variable processManagerModule.
     * @return Module of simulation processManagerModule
     */
    public ProcessManagerModule getProcessManagerModule() {
        return processManagerModule;
    }

    /**
     * Method to access the simulation variable queryProcessorModule.
     * @return Module of simulation queryProcessorModule
     */
    public QueryProcessorModule getQueryProcessorModule() {
        return queryProcessorModule;
    }

    /**
     * Method to access the simulation variable transactionalStorageModule.
     * @return Module of simulation transactionalStorageModule
     */
    public TransactionalStorageModule getTransactionalStorageModule() {
        return transactionalStorageModule;
    }

    /**
     * Method to access the simulation variable executorModule.
     * @return Module of simulation executorModule
     */
    public ExecutorModule getExecutorModule() {
        return executorModule;
    }

    /**
     * Method to access the simulation clock.
     * @return clockTime of simulation.
     */
    public double getClockTime() {
        return clockTime;
    }

    /**
     * Method to access the current number of timeout.
     * @return Numbers of timeout.
     */
    public int getTimeoutNumber() {
        return timeoutNumber;
    }

    /**
     * Method to update the number of timeout.
     */
    public void setTimeoutNumber(int timeoutNumber) {
        this.timeoutNumber = timeoutNumber;
    }

    /**
     * Method to update the number of reject or discarded queries.
     */
    public void increaseRejectQueries(){
        this.simulationStatistics.increaseDiscardedNumberOfQueries();
    }

    /**
     * Method to access the statistics of the current simulation.
     * @return Simulation Statistics.
     */
    public SimulationStatistics getSimulationStatistics() {
        return simulationStatistics;
    }
}
