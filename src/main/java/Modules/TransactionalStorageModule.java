package main.java.Modules;
import main.java.Comparators.ComparatorPriorityEvent;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.Event.QueryType;
import main.java.RandomValueGenerator;
import main.java.Simulator;

import java.util.*;

public class TransactionalStorageModule extends Module {

    private boolean processingDDL;
    private int queryDDL;
    Comparator<Event> compareAux;

    /**
     * class constructor
     * @param simulator Pointer to the simulator
     * @param randSimulator Pointer of a random value generator.
     * @param numConcurrentProcesses Number of queries in the module.
     */
    public  TransactionalStorageModule(Simulator simulator, RandomValueGenerator randSimulator, int numConcurrentProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numConcurrentProcesses;
        this.processingDDL = false;
        this.queryDDL = 0;
        compareAux = new ComparatorPriorityEvent();
        this.queue = new PriorityQueue<>(compareAux);
    }

    /**
     * Processes an arrival type of event in the transactional module.
     * @param event Event to be processed.
     */
    @Override
    public void processArrival(Event event) {
        //Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());


         processClient(event);

        //Statistics
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());
    }

    /**
     * Method to process an event in the transactional module, increase the time to the event (duration) and generate an
     * departure of that event in the same module.
     * @param event Event to be processed.
     */
    @Override
    public void processClient(Event event) {
        boolean processedEvent  = true;

        switch (event.getQuery().getType()){
            case DDL:
                ++queryDDL;
                System.out.println("Se aumenta " + queryDDL);
                if(queryDDL == 2)
                    System.out.println("I find you");
                if(busyServers > 0){

                    queue.offer(event);
                    processedEvent = false;
                }else{
                    ++busyServers;
                    processingDDL = true;
                    event.setTimeClock(event.getTimeClock()+getServiceTime(event));
                }

                break;
            case UPDATE:
            case SELECT:
            case JOIN:
                if(busyServers<numberServers && queryDDL==0 && !processingDDL ){
                    ++busyServers;
                    event.setTimeClock(event.getTimeClock()+getServiceTime(event));
                }else {
                    queue.offer(event);
                    processedEvent = false;
                }

                break;

        }
        //Output is generated
        //If the event was processed, that is, it was not added to the module's queue.
        if(processedEvent) {
            event.setEventType(EventType.DEPARTURE);
            this.simulator.addEvent(event);
        }
    }

    /**
     * Method to obtain the query duration in the module, is based on the number of blocks according to the type of query.
     * @param event Event processed.
     * @return Time in the module.
     */
    @Override
    public double getServiceTime(Event event) {
        //coordination time
        double timeTemp = numberServers*0.3;

        //time to load the blocks
        timeTemp += event.getQuery().getNumberOfBlocks()*(0.1/10);

        return timeTemp;
    }

    /**
     * Method to process the output of an event of the transactional process module, the number of occupied servers is decreased,
     * generate an arrival of that event in the executor module and statistics are updated.
     * Also check if there are events waiting to be processed in the module's local queue
     * @param event Event to be processed.
     */
    @Override
    public void processDeparture(Event event) {
        //Exit to the next event
        --busyServers;
        if(event.getQuery().getType() == QueryType.DDL)
            System.out.println(" ");

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());

        if(event.getQuery().getType() == QueryType.DDL) {
            --queryDDL;
            System.out.println("Se disminuye " + queryDDL);
            processingDDL = false;
        }


         //Exit to the next event
         event.setCurrentModule(simulator.getExecutorModule());
         event.setEventType(EventType.ARRIVAL);
         this.simulator.addEvent(event);


        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());


        if(queue.size()>0){
            Event temporal = queue.poll();
            if(temporal.getQuery().getType() == QueryType.DDL) {
                --queryDDL;
            }
            processClient(temporal);
        }
        //Check the local queue
        //this.processNextLocalQueueEvent();
    }

    /**
     * Process event that made a different "timeout"
     * @param event event that made timeout
     * @param isQueue Boolean to know if it made timeout in queue
     */
    @Override
    public void processTimeoutEvent(Event event, boolean isQueue){
        if(event.getQuery().getType() == QueryType.DDL){
            System.out.println("Se disminuye en timeout " + queryDDL);
            --queryDDL;
        }
        if(!isQueue){
            --this.busyServers;
            if(event.getQuery().getType() == QueryType.DDL){
                processingDDL = false;
            }
        }
    }

    /**
     * Decrease DDL number
     */
    public void decreaseDDLNumber(){
        --queryDDL;
    }

}
