import java.util.PriorityQueue;

public abstract class Module {
    private int queueLength;
    private int numberOfServers;
    private int occupability;
    private PriorityQueue<Event> queue;
    private PriorityQueue<Event> globalQueue;
    private ModuleStatistics statisticsOfModule;

    public Module(PriorityQueue<Event> global){
        globalQueue = global;
    }

    public void processInput(){

    }

    public void processOutput(){

    }
}
