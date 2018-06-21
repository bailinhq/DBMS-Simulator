package main.java.Modules;

import java.util.PriorityQueue;

public class ExecutorModule extends Module {


    ExecutorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
        this.queue = new PriorityQueue<>(new ComparatorNormalEvent());
    }

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

    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setTimeClock(event.getTimeClock()+getServiceTime(event));
        //Output is generated
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);

    }

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
            case UPDATE:
                //Update database schema 1 second
                timeTemp += 1.0;
                break;
            case SELECT:
            case JOIN:
                break;
        }

        return timeTemp;
    }


    @Override
    public void processDeparture(Event event) {

        --busyServers;

        //Exit to the next event
        //event.setCurrentModule(simulator.getTransactionalStorageModule());

        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getClientCommunicationsManagerModule());
            event.setEventType(EventType.RETURN);
            this.simulator.addEvent(event);
        }

        boolean isTimeOut = true;
        while (this.queue.size()>0 && isTimeOut){
            Event temporal = this.queue.poll();
            if(!this.simulator.isTimeOut(event)){
                processClient(temporal);
                isTimeOut = false;
            }else {
                simulator.setTimeoutNumber(simulator.getTimeoutNumber() + 1);
            }
        }

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());
    }

}
