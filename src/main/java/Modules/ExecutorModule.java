package main.java.Modules;

import main.java.Comparators.ComparatorFIFO;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.RandomValueGenerator;
import main.java.Simulator;

import java.util.PriorityQueue;

public class ExecutorModule extends Module {

    /**
     * class constructor
     * @param simulator Pointer to the simulator
     * @param randSimulator Pointer of a random value generator.
     * @param numProcesses Number of process in the module.
     */
    public ExecutorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
        this.queue = new PriorityQueue<>(new ComparatorFIFO());
    }

    /**
     * Processes an arrival type of event in the executor module.
     * @param event Event to be processed.
     */
    @Override
    public void processArrival(Event event) {
        //Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());

        if(busyServers < numberServers){
            processClient(event);
        }else{
            queue.offer(event);
        }

        //Statistics
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());
    }

    /**
     * Method to process an event in the executor module, increase the time to the event (duration) and generate an
     * departure of that event in the same module.
     * @param event Event to be processed.
     */
    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setTimeClock(event.getTimeClock()+getServiceTime(event));
        //Output is generated
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);

    }

    /**
     * Method to obtain the query duration in the module, is based on the number of blocks according to the type of query.
     * @param event Event processed.
     * @return Time in the module.
     */
    @Override
    public double getServiceTime(Event event) {

        double numBlocks = event.getQuery().getNumberOfBlocks();

        //B^2 milliseconds = (B^2)/1000  seconds
        double timeTemp = (numBlocks * numBlocks)/1000.0;

        switch (event.getQuery().getType()){
            case DDL:
                //Update database schema 1/2 second
                timeTemp += 0.5;
                break;
            case SELECT:
                break;
            case UPDATE:
                //Update database schema 1 second
                timeTemp += 1.0;
                break;
            case JOIN:
                break;
        }

        return timeTemp;
    }

    /**
     * Method to process the output of an event of the executor module, the number of occupied servers is decreased,
     * generate a return of that event in the client module and statistics are updated.
     * Also check if there are events waiting to be processed in the module's local queue
     * @param event Event to be processed.
     */
    @Override
    public void processDeparture(Event event) {

        --busyServers;

         //Exit to the next event
         event.setCurrentModule(simulator.getClientCommunicationsManagerModule());
         event.setEventType(EventType.RETURN);
         this.simulator.addEvent(event);


        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());

        //Check the local queue
        this.processNextLocalQueueEvent();
    }

}
