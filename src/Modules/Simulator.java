package Modules;

import Modules.*;

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
    private double clocktime;

    private Random randomGenerator;

    public Simulator(){
        randomGenerator = new Random(0);
        valueGenerator = new RandomValueGenerator();
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
        return new Event(query, clocktime, EventType.ARRIVAL, clientCommunicationsManagerModule);
    }

    private void simulate(){

    }

    public void setParameters(){

    }
}
