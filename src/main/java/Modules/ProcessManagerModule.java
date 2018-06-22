package main.java.Modules;

import main.java.Comparators.ComparatorFIFO;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.RandomValueGenerator;
import main.java.Simulator;

import java.util.PriorityQueue;

public class ProcessManagerModule extends Module {

    /**
     * class constructor
     * @param simulator Pointer to the simulator
     * @param randSimulator Pointer of a random value generator.
     */
    public ProcessManagerModule(Simulator simulator, RandomValueGenerator randSimulator) {
        super(simulator, randSimulator);
        this.numberServers = 1;
        this.queue = new PriorityQueue<>(new ComparatorFIFO());
    }

    /**
     * Processes an arrival type of event in the process module.
     * @param event Event to be processed.
     */
    @Override
    public void processArrival(Event event) {
        //Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());

        if(this.busyServers < this.numberServers){
            processClient(event);
        }else{
            queue.offer(event);
        }

        //Statistics
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());
    }

    /**
     * Method to process an event in the process module, increase the time to the event (duration) and generate an
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
     * Method to obtain the query duration in the module, is based in a normal distribution.
     * @param event Event processed.
     * @return Time in the module.
     */
    @Override
    public double getServiceTime(Event event) {
        return this.randomValueGenerator.generateNormalDistributionValue(1,0.01);
    }

    /**
     * Method to process the output of an event of the process module, the number of occupied servers is decreased,
     * generate an arrival of that event in the query process module and statistics are updated.
     * Also check if there are events waiting to be processed in the module's local queue
     * @param event Event to be processed.
     */
    @Override
    public void processDeparture(Event event) {
        --busyServers;

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());

        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getQueryProcessorModule());
            event.setEventType(EventType.ARRIVAL);
            this.simulator.addEvent(event);
        }

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());

        //Check the local queue
        this.processNextLocalQueueEvent();

    }




}
