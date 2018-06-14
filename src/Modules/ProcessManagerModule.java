package Modules;

import java.util.PriorityQueue;

public class ProcessManagerModule extends Module {

    ProcessManagerModule(Simulator simulator, RandomValueGenerator randSimulator) {
        super(simulator, randSimulator);
        this.numberServers = 1;
    }

    @Override
    public void processArrival(Event event) {
        System.out.println("Llega cliente al modulo 2");
        if(busyServers < numberServers){
            processClient(event);
        }else{
            queue.offer(event);
        }
    }

    @Override
    public void processDeparture(Event event) {
        --busyServers;
        if(queue.size()>0){
            Event temporal = queue.poll();
            processClient(temporal);
        }
        //Exit to the next event
        event.setCurrentModule(simulator.getQueryProcessorModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }

    @Override
    public double getServiceTime(Event event) {
        return this.randomValueGenerator.generateNormalDistributionValue(1,0.01);
    }

    @Override
    public void processClient(Event event) {
        ++busyServers;

        event.setTimeClock(event.getTimeClock()+getServiceTime(event));
        System.out.println("\n\n\n\n"+event.getTimeClock()+"\n\n\n\n");
        //Output is generated
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);
    }
}
