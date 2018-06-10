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
    private int maxConcurrentConnections;
    private double timeout;
    private double clockTime;
    private int p;

    private SimulationStatistics simulationStatistics;

    private Random randomGenerator;

    public Simulator(){
        randomGenerator = new Random(0);
        valueGenerator = new RandomValueGenerator();
        simulationStatistics = new SimulationStatistics();
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

//    private Event generateEvent(){
//        Query query = new Query();
//        Event event = new Event(query, EventType.ARRIVAL);
//        return event;
//    }


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

    private void simulate(){
        
    }

    public void setParameters(){

    }

    public void addEvent(Event event){
        queue.offer(event);
    }

    public  void increaseRejectQueries(){
        this.simulationStatistics.increaseDiscardedNumberOfQueries();
    }

    public double getClockTime() {
        return clockTime;
    }
}
