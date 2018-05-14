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
        return new Query(0, 0);
    }

    private void simulate(){
        
    }

    public void setParameters(){

    }
}
