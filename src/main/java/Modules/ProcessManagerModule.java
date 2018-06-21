package main.java.Modules;

import java.util.PriorityQueue;

public class ProcessManagerModule extends Module {

    ProcessManagerModule(Simulator simulator, RandomValueGenerator randSimulator) {
        super(simulator, randSimulator);
        this.numberServers = 1;
        this.queue = new PriorityQueue<>(new ComparatorNormalEvent());
    }

    @Override
    public void processArrival(Event event) {
        //System.out.println("Llega cliente al modulo 2 -> "+event.getTimeClock());

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());

        if(this.busyServers < this.numberServers){
            processClient(event);
            //System.out.println("Tiempo servicio -> "+event.getTimeClock()+"\n");
        }else{
           // System.out.println("Entra a cola "+simulator.getClockTime()+"\n");
            queue.offer(event);
        }
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
        return this.randomValueGenerator.generateNormalDistributionValue(1,0.01);
    }

    @Override
    public void processDeparture(Event event) {
        //System.out.println("Sale cliente al modulo 2 -> "+event.getTimeClock()+"\n\n");
        --busyServers;

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());

        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getQueryProcessorModule());
            event.setEventType(EventType.ARRIVAL);
            this.simulator.addEvent(event);
        }

        boolean noTimeOut = false;
        while (this.queue.size()>0 && !noTimeOut){
            Event temporal = this.queue.poll();
            //System.out.println("Sale de la cola "+simulator.getClockTime()+"\n");
            if(!this.simulator.isTimeOut(event)){
                processClient(temporal);
                noTimeOut = true;
            }
        }

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());

    }




}
