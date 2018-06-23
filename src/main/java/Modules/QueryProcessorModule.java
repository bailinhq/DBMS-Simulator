package main.java.Modules;

import main.java.Comparators.ComparatorFIFO;
import main.java.Event.Event;
import main.java.Event.EventType;
import main.java.RandomValueGenerator;
import main.java.Simulator;

import java.util.PriorityQueue;

public class QueryProcessorModule extends Module {

    //Capacity for n processes
    /**
     * class constructor
     * @param simulator Pointer to the simulator
     * @param randSimulator Pointer of a random value generator.
     * @param numProcesses Number of queries in the module.
     */
    public QueryProcessorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
        this.queue = new PriorityQueue<>(new ComparatorFIFO());
    }


    /**
     * Processes an arrival type of event in the query process module.
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
     * Method to process an event in the query process module, increase the time to the event (duration) and generate an
     * departure of that event in the same module.
     * @param event Event to be processed.
     */
    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setTimeClock(event.getTimeClock()+getServiceTime(event));
        //System.out.println(event.getTimeClock() - event.getQuery().getQueryStatistics().getArrivalTime()+"Clock Query\n\n");
        //Output is generated
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);
    }

    /**
     * Method to obtain the query duration in the module, the distribution to calculate the time is different for each query.
     * @param event Event processed.
     * @return Time in the module.
     */
    @Override
    public double getServiceTime(Event event) {
        double processingTime = 0.0;
        //lexical validation
        processingTime += 0.1;

        //syntactic validation --> uniform random value 0<=x<=1 seconds
        processingTime += this.randomValueGenerator.generateUniformDistributionValue(0,1);

        //semantic validation --> uniform random value 0<=x<=2 seconds
        processingTime += this.randomValueGenerator.generateUniformDistributionValue(0,2);

        //permit verification --> exponential random value with mean 0.7 seconds -> 0.7 = 1/lambda -> lambda = 1/0.7
        processingTime+= this.randomValueGenerator.generateExponentialDistributionValue(1.0/0.7);

        //TODO definir si realmente la consulta debe saber cuanto dura para que la optimizen.
        //query optimization
        processingTime += event.getQuery().getTimeOptimization();

        return  processingTime;
    }


    /**
     * Method to process the output of an event of the query process module, the number of occupied servers is decreased,
     * generate an arrival of that event in the transactional module and statistics are updated.
     * Also check if there are events waiting to be processed in the module's local queue
     * @param event Event to be processed.
     */
    @Override
    public void processDeparture(Event event) {
        //Exit to the next event
        --busyServers;

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());


        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getTransactionalStorageModule());
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
