package Modules;

import Modules.*;
import Statistics.SimulationStatistics;

import java.util.PriorityQueue;
import java.util.Random;

public class Simulator {
    private RandomValueGenerator valueGenerator;
    private ClientCommunicationsManagerModule clientCommunicationsManagerModule;
    private ProcessManagerModule processManagerModule;
    private QueryProcessorModule queryProcessorModule;
    private TransactionalStorageModule transactionalStorageModule;
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
    private int p;

    private SimulationStatistics simulationStatistics;

    private Random randomGenerator;

    public Simulator(Object parameters[]){
        this.setUp(parameters);
    }

    public void setUp(Object parameters[]){
        randomGenerator = new Random(0);
        valueGenerator = new RandomValueGenerator();
        clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this, valueGenerator, 0);
        processManagerModule = new ProcessManagerModule(this, valueGenerator);
        queryProcessorModule = new QueryProcessorModule(this, valueGenerator , 0);
        transactionalStorageModule = new TransactionalStorageModule(this, valueGenerator, 0);
        queue = new PriorityQueue<>();
        this.setParameters(parameters);
    }

    public void run(){

    }

    private Query generateQuery(){
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

    private Event generateEvent(){
        Query query = generateQuery();
        return new Event(query, clockTime, EventType.ARRIVAL, clientCommunicationsManagerModule);
    }

    private void simulate(){

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
    }

    public void addEvent(Event event){
        queue.offer(event);
    }

    public RandomValueGenerator getValueGenerator() {
        return valueGenerator;
    }

    public ClientCommunicationsManagerModule getClientCommunicationsManagerModule() {
        return clientCommunicationsManagerModule;
    }

    public ProcessManagerModule getProcessManagerModule() {
        return processManagerModule;
    }

    public QueryProcessorModule getQueryProcessorModule() {
        return queryProcessorModule;
    }

    public TransactionalStorageModule getTransactionalStorageModule() {
        return transactionalStorageModule;
    }


    public void setParameters(){

    }


    public  void increaseRejectQueries(){
        this.simulationStatistics.increaseDiscardedNumberOfQueries();
    }

    public double getClockTime() {
        return clockTime;
    }
}
