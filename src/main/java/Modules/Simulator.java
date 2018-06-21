package main.java.Modules;

import main.java.Controller.Application;
import main.java.Interface.InterfaceController;
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
    public int numClientes;
    public int llegan;

    private int numberOfSimulations;
    private double maxSimulationTime;
    private boolean delay;
    private int maxConcurrentConnectionsSystem;
    private int maxConcurrentConnectionsQueries;
    private int maxQueriesInTransactions;
    private int maxQueriesInExecutor;
    private double timeout;
    private double clockTime;
    private boolean firstEvent;

    private SimulationStatistics simulationStatistics;
    private InterfaceController interfaceController;
    private Application application;
    private Random randomGenerator;

    public Simulator(InterfaceController interfaceController){
        this.valueGenerator = new RandomValueGenerator();
        this.interfaceController = interfaceController;

        this.clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this,valueGenerator,15);
        this.processManagerModule = new ProcessManagerModule(this,valueGenerator);
        this.queryProcessorModule = new QueryProcessorModule(this,valueGenerator,5);
        this.executorModule = new ExecutorModule(this,valueGenerator,5);
        this.simulationStatistics = new SimulationStatistics();
        this.queue = new PriorityQueue<>(new ComparatorNormalEvent());
        this.transactionalStorageModule =  new TransactionalStorageModule(this,valueGenerator,5);
        this.timeout =5;
        this.firstEvent = true;
        this.clockTime = 5;
    }



    public void setParameters(Object parameters[]){
        numberOfSimulations = (Integer) parameters[0];
        maxSimulationTime = (Double) parameters[1];
        delay = (Boolean) parameters[2];
        maxConcurrentConnectionsSystem = (Integer) parameters[3];
        maxConcurrentConnectionsQueries = (Integer) parameters[4];
        maxQueriesInTransactions = (Integer) parameters[5];
        maxQueriesInExecutor = (Integer) parameters[6];
        timeout = (Double) parameters[7];

        setUp();
    }

    private void setUp(){

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
    }



    public void generateNewEvent(){
        Query query = generateQuery();


        double timeTemp = 0;
        if(!firstEvent){
            //30 connections per minute -> 1/lambda = 30 connections/min -> 1/lambda = 0.5connections/sec -> lambda = 2
            timeTemp = valueGenerator.generateExponentialDistributionValue(0.5 );

        }else {
            firstEvent = false;
        }

        queue.offer(new Event(query, clockTime+timeTemp, EventType.ARRIVAL, clientCommunicationsManagerModule));

        //System.out.println("Tipo->" + query.getType()+"\n\n");
    }



    private Query generateQuery(){
        randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
        Query query;
        if (random <= 0.30){
            query = new Query(QueryType.SELECT);
        } else if (random <= 0.55){
            query = new Query(QueryType.UPDATE);
        } else if (random <= 0.90){
            query = new Query(QueryType.JOIN);
        } else {
            query = new Query(QueryType.DDL);
        }
        return query;
    }

    void addEvent(Event event){
        if(isTimeOut(event))
        {
            this.simulationStatistics.increaseTimeLife(event.getQuery().getQueryStatistics().getArrivalTime(),event.getQuery().getQueryStatistics().getDepartureTime());
        }else {
            queue.offer(event);
        }
    }

    public void checkTimeOutSystemQueues(){
        checkTimeOutQueue(this.queryProcessorModule.queue);
        checkTimeOutQueue(this.queryProcessorModule.queue);
        checkTimeOutQueue(this.transactionalStorageModule.queue);
        checkTimeOutQueue(this.executorModule.queue);
    }

    public void checkTimeOutQueue(PriorityQueue<Event> queue){
        Iterator<Event> iterator = queue.iterator();
        while (iterator.hasNext())
        {
            Event event = iterator.next();
            if(isTimeOut(event)){
                this.simulationStatistics.increaseTimeLife(event.getQuery().getQueryStatistics().getArrivalTime(),event.getQuery().getQueryStatistics().getDepartureTime());
                iterator.remove();
                System.out.println("Se elimina");
            }
        }
    }

    public boolean isTimeOut(Event event){
        if(event.getQuery().getQueryStatistics().getTimeInSystem()> this.timeout){
            System.out.println("\033[31mHay timeout");
            return true;
        }else{
            return false;
        }
    }


    public void run(){
        int numberSimulation = 0;
        while(numberSimulation < this.numberOfSimulations) {
            simulate();
            ++numberSimulation;
        }
    }

    public void simulate(){
        generateNewEvent();
        numClientes = 0;
        llegan = 0;
        while (this.clockTime <= this.maxSimulationTime){
            Event event = queue.poll();
            checkTimeOutSystemQueues();
            if(event!=null){
                this.clockTime = event.getTimeClock();
                this.updateData();
                delay();
                event.getCurrentModule().processEvent(event);
            }else{
                clockTime = maxSimulationTime+1;
            }
        }
        System.out.println("Clientes atendidos " + numClientes + "\n Rechazados " + this.simulationStatistics.getDiscardedNumberOfQueries()+"\nLlega "+ llegan);
        System.out.println("El total de consultas atendidas en Modulo Clientes fue "+ this.clientCommunicationsManagerModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio en Modulo Clientes fue "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageSizeQueue());
        System.out.println("El tiempo de DDL en Clientes es "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.DDL));
        System.out.println("El tiempo de SE en Clientes es "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.SELECT));
        System.out.println("El tiempo de UP en Clientes es "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.UPDATE));
        System.out.println("El tiempo de JOIN en Clientes es "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.JOIN));
        clientCommunicationsManagerModule.statisticsOfModule.printData();

        System.out.println("\n\nEl total de consultas atendidas en Modulo Procesos fue "+ this.processManagerModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Procesos fue "+ this.processManagerModule.statisticsOfModule.getAverageSizeQueue());
        System.out.println("El tiempo de DDL en Procesos es "+ this.processManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.DDL));
        System.out.println("El tiempo de SE en Procesos es "+ this.processManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.SELECT));
        System.out.println("El tiempo de UP en Procesos es "+ this.processManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.UPDATE));
        System.out.println("El tiempo de JOIN en Procesos es "+ this.processManagerModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.JOIN));
        processManagerModule.statisticsOfModule.printData();

        System.out.println("\n\nEl total de consultas atendidas en Modulo Consultas fue "+ this.queryProcessorModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Consultas fue "+ this.queryProcessorModule.statisticsOfModule.getAverageSizeQueue());
        System.out.println("El tiempo de DDL en Consultas es "+ this.queryProcessorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.DDL));
        System.out.println("El tiempo de SE en Consultas es "+ this.queryProcessorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.SELECT));
        System.out.println("El tiempo de UP en Consultas es "+ this.queryProcessorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.UPDATE));
        System.out.println("El tiempo de JOIN en Consultas es "+ this.queryProcessorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.JOIN));
        queryProcessorModule.statisticsOfModule.printData();


        System.out.println("\n\nEl total de consultas atendidas en Modulo Transacciones fue "+ this.transactionalStorageModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Transacciones fue "+ this.transactionalStorageModule.statisticsOfModule.getAverageSizeQueue());
        System.out.println("El tiempo de DDL en Transacciones es "+ this.transactionalStorageModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.DDL));
        System.out.println("El tiempo de SE en Transacciones es "+ this.transactionalStorageModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.SELECT));
        System.out.println("El tiempo de UP en Transacciones es "+ this.transactionalStorageModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.UPDATE));
        System.out.println("El tiempo de JOIN en Transacciones es "+ this.transactionalStorageModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.JOIN));
        transactionalStorageModule.statisticsOfModule.printData();

        System.out.println("\n\nEl total de consultas atendidas en Modulo Exe fue "+ this.executorModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Exe fue "+ this.executorModule.statisticsOfModule.getAverageSizeQueue());
        System.out.println("El tiempo de DDL en Exe es "+ this.executorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.DDL));
        System.out.println("El tiempo de SE en Exe es "+ this.executorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.SELECT));
        System.out.println("El tiempo de UP en Exe es "+ this.executorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.UPDATE));
        System.out.println("El tiempo de JOIN en Exe es "+ this.executorModule.statisticsOfModule.getAverageTimeInModuleOfQuery(QueryType.JOIN));
        executorModule.statisticsOfModule.printData();

        System.out.println("\nEl tiempo de vida promedio de una consulta es "+ this.simulationStatistics.getTimeLifeOfQuery());
        System.out.println("FIN");
    }



    private int[] getModulesQueueLength(){
        int queueLength[] = new int[5];
        queueLength[M_CLIENTS] = this.clientCommunicationsManagerModule.queue.size();
        queueLength[M_PROCESSES] = this.processManagerModule.queue.size();
        queueLength[M_QUERIES] = this.queryProcessorModule.queue.size();
        queueLength[M_TRANSACTIONS] = this.transactionalStorageModule.queue.size();
        queueLength[M_EXECUTIONS] = this.executorModule.queue.size();
        return queueLength;
    }

    private int[] getDDLNumber(){
        int DDLNumber[] = new int[5];
        DDLNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfDDL();
        DDLNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfDDL();
        return DDLNumber;
    }

    private int[] getUpdateNumber(){
        int updateNumber[] = new int[5];
        updateNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfUPDATE();
        updateNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfUPDATE();
        return updateNumber;
    }

    private int[] getJoinNumber(){
        int joinNumber[] = new int[5];
        joinNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfJOIN();
        joinNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfJOIN();
        return joinNumber;
    }

    private int[] getSelectNumber(){
        int selectNumber[] = new int[5];
        selectNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getNumberOfSELECT();
        selectNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getNumberOfSELECT();
        return selectNumber;
    }

    private void updateData(){
        this.interfaceController.updateClock(this.clockTime);
        this.interfaceController.updateDiscarded(this.simulationStatistics.getDiscardedNumberOfQueries());
        this.interfaceController.showDDLNumber(this.getDDLNumber());
        this.interfaceController.showJoinNumber(this.getJoinNumber());
        this.interfaceController.showSelectNumber(this.getSelectNumber());
        this.interfaceController.showUpdateNumber(this.getUpdateNumber());
        this.interfaceController.showQueueLength(this.getModulesQueueLength());
        this.interfaceController.updateSimulationNumber(this.numberOfSimulations);
    }

    private void delay(){
        /*if(delay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    public RandomValueGenerator getValueGenerator() {
        return valueGenerator;
    }

    ClientCommunicationsManagerModule getClientCommunicationsManagerModule() {
        return clientCommunicationsManagerModule;
    }

    ProcessManagerModule getProcessManagerModule() {
        return processManagerModule;
    }

    QueryProcessorModule getQueryProcessorModule() {
        return queryProcessorModule;
    }

    TransactionalStorageModule getTransactionalStorageModule() {
        return transactionalStorageModule;
    }

    ExecutorModule getExecutorModule() {
        return executorModule;
    }

    public double getClockTime() {
        return clockTime;
    }

    public  void increaseRejectQueries(){
        this.simulationStatistics.increaseDiscardedNumberOfQueries();
    }

    public void setNumberOfSimulations(int numberOfSimulations) {
        this.numberOfSimulations = numberOfSimulations;
    }

    public void setMaxSimulationTime(double maxSimulationTime) {
        this.maxSimulationTime = maxSimulationTime;
    }

    public SimulationStatistics getSimulationStatistics() {
        return simulationStatistics;
    }
}
