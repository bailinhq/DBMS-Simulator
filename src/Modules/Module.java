package Modules;

import Statistics.ModuleStatistics;

import java.util.PriorityQueue;

public abstract class Module {
    Simulator simulator;
    //TODO definir si mejor hacer un enum o tener un entero para comparar más facil
    public int currentModule;

    public int queueLength;
    public int numberServers;
    public int busyServers;
    public PriorityQueue<Event> queue;
    public RandomValueGenerator randomValueGenerator;


    private ModuleStatistics statisticsOfModule;

    Module(Simulator simulator, RandomValueGenerator randSimulator) {
        this.simulator = simulator;
        this.randomValueGenerator = randSimulator;
        this.busyServers = 0;
        this.queueLength = 0;
        this.queue = new PriorityQueue<>();
    }

    public abstract void processArrival(Event event);
    public abstract void processDeparture(Event event);
    public abstract double getServiceTime(Event event);
    public abstract  void processClient(Event event);


    public void processEvent(Event event){
        switch (event.getEventType()){
            case ARRIVAL: processArrival(event);
            break;
            case DEPARTURE: processDeparture(event);
            break;
            default:
                System.out.println("Error, processEvent");
                break;
        }
    }

    public int getCurrentModuleType() {
        return currentModule;
    }
}
