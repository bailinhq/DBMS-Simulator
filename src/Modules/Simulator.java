package Modules;

import Statistics.SimulationStatistics;

import java.util.PriorityQueue;
import java.util.Random;

public class Simulator {
    private RandomValueGenerator valueGenerator;
    private ClientCommunicationsManagerModule clientCommunicationsManagerModule;
    private ProcessManagerModule processManagerModule;
    private QueryProcessorModule queryProcessorModule;
    private TransactionalStorageModule transactionalStorageModule;
    private ExecutorModule executorModule;
    private PriorityQueue<Event> queue;

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
    private Random randomGenerator;

    public Simulator(){
        this.valueGenerator = new RandomValueGenerator();

        this.clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this,valueGenerator,10);
        this.processManagerModule = new ProcessManagerModule(this,valueGenerator);
        this.queryProcessorModule = new QueryProcessorModule(this,valueGenerator,5);
        this.executorModule = new ExecutorModule(this,valueGenerator,5);
        this.simulationStatistics = new SimulationStatistics();
        this.queue = new PriorityQueue<>();
        this.transactionalStorageModule =  new TransactionalStorageModule(this,valueGenerator,5);
        this.timeout = 100;
        this.firstEvent = true;
    }
    public Simulator(Object parameters[]){
        this.setUp(parameters);
    }

    public void setUp(Object parameters[]){
        //TODO cambiar los valores que reciben los modulos
        randomGenerator = new Random(0);
        valueGenerator = new RandomValueGenerator();
        clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this, valueGenerator, 0);
        processManagerModule = new ProcessManagerModule(this, valueGenerator);
        queryProcessorModule = new QueryProcessorModule(this, valueGenerator , 0);
        transactionalStorageModule = new TransactionalStorageModule(this, valueGenerator, 0);
        executorModule = new ExecutorModule(this, valueGenerator, 0);

        queue = new PriorityQueue<>();
        this.setParameters(parameters);
    }

    public void generateNewEvent(){
        Query query = generateQuery();


        double timeTemp = 0;
        if(!firstEvent){
            //30 connections per minute -> 1/lambda = 30 connections/min -> 1/lambda = 0.5connections/sec -> lambda = 2
            timeTemp = valueGenerator.generateExponentialDistributionValue(2);
        }else {
            firstEvent = false;
        }

        queue.offer(new Event(query, clockTime+timeTemp, EventType.ARRIVAL, clientCommunicationsManagerModule));
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




    private void setParameters(Object parameters[]){
        numberOfSimulations = (Integer) parameters[0];
        maxSimulationTime = (Double) parameters[1];
        delay = (Boolean) parameters[2];
        maxConcurrentConnectionsSystem = (Integer) parameters[3];
        maxConcurrentConnectionsQueries = (Integer) parameters[4];
        maxQueriesInTransactions = (Integer) parameters[5];
        maxQueriesInExecutor = (Integer) parameters[6];
        timeout = (Double) parameters[7];
    }

    void addEvent(Event event){
        if (!isTimeOut(event))
            queue.offer(event);
    }

    private RandomValueGenerator getValueGenerator() {
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

    public boolean isTimeOut(Event event){
        if(event.getQuery().getQueryStatistics().getTimeInSystem()> this.timeout){
            return true;
        }else{
            return false;
        }
    }

    public void setNumberOfSimulations(int numberOfSimulations) {
        this.numberOfSimulations = numberOfSimulations;
    }

    public void setMaxSimulationTime(double maxSimulationTime) {
        this.maxSimulationTime = maxSimulationTime;
    }

    public void run(){
        int numberSimulation = 0;
        while(numberSimulation < this.numberOfSimulations) {
            simulate();
            ++numberSimulation;
        }
    }

    private void simulate(){
        generateNewEvent();
        while (this.clockTime <= this.maxSimulationTime){
            Event event = queue.poll();
            if(event!=null){
                this.clockTime = event.getTimeClock();
                event.getCurrentModule().processEvent(event);
            }else{
                clockTime = maxSimulationTime+1;
            }
        }
    }
}
