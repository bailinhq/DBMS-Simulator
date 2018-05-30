package Modules;

import Modules.*;

import java.util.PriorityQueue;

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
    private int p;

    public Simulator(){
        valueGenerator = new RandomValueGenerator();
    }

    public void run(){

    }

    private Query generateQuery(){
        Query query = new Query(QueryType.DDL);
        return query;
    }

    private void simulate(){
        
    }

    public void setParameters(){

    }
}
