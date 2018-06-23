package main.java.Modules;

import main.java.Comparators.ComparatorFIFO;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.RandomValueGenerator;
import main.java.Simulator;

import java.util.PriorityQueue;

public class ClientCommunicationsManagerModule extends Module {

    /**
     * class constructor
     * @param simulator Pointer to the simulator
     * @param randSimulator Pointer of a random value generator.
     * @param numConnections Number of connections of the module.
     */
    public ClientCommunicationsManagerModule(Simulator simulator, RandomValueGenerator randSimulator, int numConnections) {
        //Receive k connections
        super(simulator, randSimulator);
        this.numberServers = numConnections;
        this.queue = new PriorityQueue<>(new ComparatorFIFO());
    }

    /**
     * Processes an arrival type of event in the client module.
     * @param event Event to be processed.
     */
    @Override
    public void processArrival(Event event) {
        //Statistics
        event.getQuery().getQueryStatistics().setArrivalTime(this.simulator.getClockTime());

        if(this.busyServers < numberServers){
            processClient(event);
        }else{
            this.simulator.increaseRejectQueries();
        }
        //A new arrival is generated
        this.simulator.generateNewEvent();

        //Statistics
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());
    }

    /**
     * Method to process an event in the connections module, increase the time to the event (duration) and generate an
     * arrival of that event in the process module.
     * @param event Event to be processed.
     */
    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setCurrentModule(simulator.getProcessManagerModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }

    /**
     * Method to obtain the attention time of a query.
     * @param event Event processed.
     * @return Time in the module.
     */
    @Override
    public double getServiceTime(Event event) { return 0.0; }

    /**
     * Method that processes the return to the client module, increases the time of data transition depending on the
     * type of query.
     * @param event Event processed.
     */
    public void processReturn(Event event){
        //Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());

        //transmission time R = numbers of blocks
        double timeTemp = event.getQuery().getNumberOfBlocks();

        event.setTimeClock(event.getTimeClock()+timeTemp);
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);
    }


    /**
     * Method to process the output of an event of the client module, the number of occupied servers is decreased
     * and statistics are updated.
     * @param event Event to be processed.
     */
    @Override
    public void processDeparture(Event event) {
        --busyServers;

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTime(),event.getQuery().getQueryStatistics().getDepartureTime());
        this.simulator.getSimulationStatistics().increaseTimeLife(event.getQuery().getQueryStatistics().getArrivalTime(),event.getQuery().getQueryStatistics().getDepartureTime());
    }

    /**
     * Method that indicates the way to process an event, depending on the type (arrival, departure or return to the
     * module).
     * @param event Event to be processed.
     */
    @Override
    public void processEvent(Event event) {
        switch (event.getEventType()){
            case ARRIVAL: processArrival(event);
                break;
            case DEPARTURE: processDeparture(event);
                break;
            case RETURN: processReturn(event);
                break;
                default:
                    break;
        }
    }

    public void processTimeoutEvent(){
        --this.busyServers;
    }

}
