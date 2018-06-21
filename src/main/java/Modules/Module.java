package main.java.Modules;

import main.java.Event.Event;
import main.java.RandomValueGenerator;
import main.java.Simulator;
import main.java.Statistics.ModuleStatistics;

import java.util.PriorityQueue;

public abstract class Module {
    Simulator simulator;
    //TODO definir si mejor hacer un enum o tener un entero para comparar más facil
    protected int currentModule;

    protected long queueLength;
    protected int numberServers;
    protected int busyServers;

    protected PriorityQueue<Event> queue;
    protected RandomValueGenerator randomValueGenerator;
    protected ModuleStatistics statisticsOfModule;

    /**
     * Initializes a Module with a Simulator and a RandomValueGenerator.
     * @param simulator     A Global simulator for all modules.
     * @param randSimulator A RandomValueGenerator for all modules.
     */
    Module(Simulator simulator, RandomValueGenerator randSimulator) {
        this.simulator = simulator;
        this.randomValueGenerator = randSimulator;
        this.busyServers = 0;
        this.queueLength = 0;
        this.statisticsOfModule = new ModuleStatistics();
    }

    /**
     * Processes an arrival type of Event.
     * @param event Event to be processed.
     */
    protected abstract void processArrival(Event event);

    /**
     * Procceses a departure type of Event.
     * @param event Event to be processed.
     */
    protected abstract void processDeparture(Event event);

    /**
     * Calculates the time it took to process the event.
     * @param event Event processed.
     * @return Duration of the process.
     */
    protected abstract double getServiceTime(Event event);

    /**
     * Processes an arrival type of event, gets the service time and changes the event type to DEPARTURE.
     * @param event Event to be processed.
     */
    protected abstract void processClient(Event event);

    /**
     * Processes an event according to its type. If the event is of type ARRIVAL, it executes processArrival, else it
     * executes processsDeparture. The default case is an error processing the event.
     * @param event Event to be processed.
     */
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

    /**
     * Returns the StatisticsModule of this Module.
     * @return StatisticsModule
     */
    public ModuleStatistics getStatisticsOfModule(){
        return statisticsOfModule;
    }

    /**
     * Returns the queue of this module.
     * @return queue of this module.
     */
    public PriorityQueue<Event> getQueue() {
        return queue;
    }
}
