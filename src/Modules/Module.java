package Modules;

import Statistics.ModuleStatistics;

import java.util.PriorityQueue;

public abstract class Module {
    Simulator simulator;

    public PriorityQueue<Event> globalQueue;
    public int queueLength;
    public int numberServers;
    public int busyServers;
    public PriorityQueue<Event> queue;


    private ModuleStatistics statisticsOfModule;

    Module(Simulator simulator) {
        this.simulator = simulator;
    }

    public abstract void processArrival(Event event);
    public abstract void processDeparture(Event event);

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


}
