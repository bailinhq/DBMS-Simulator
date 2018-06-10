package Modules;

import java.util.PriorityQueue;

public class ProcessManagerModule extends Module {

    ProcessManagerModule(Simulator simulator, RandomValueGenerator randSimulator) {
        super(simulator, randSimulator);
        this.numberServers = 1;
    }

    @Override
    public void processArrival(Event event) {
        if(busyServers>0){
            queue.offer(event);
        }else{
            ++busyServers;
            event.setTimeClock(simulator.getClockTime()+this.randomValueGenerator.generateNormalDistributionValue(1,0.01));
            //Output is generated
            event.setEventType(EventType.DEPARTURE);
            this.simulator.addEvent(event);
        }
    }

    @Override
    public void processDeparture(Event event) {
        //Exit to the next event
        event.setCurrentModule(simulator.getQueryProcessorModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }
}
