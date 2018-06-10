package Modules;

import java.util.PriorityQueue;

public class TransactionalStorageModule extends Module {

    TransactionalStorageModule(Simulator simulator, RandomValueGenerator randSimulator, int numConcurrentProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numConcurrentProcesses;
    }


    @Override
    public void processArrival(Event event) {
        if(busyServers>0){
            queue.offer(event);
        }else{
            ++busyServers;
            event.setTimeClock(simulator.getClockTime());

            //Output is generated
            event.setEventType(EventType.DEPARTURE);
            this.simulator.addEvent(event);
        }
    }

    @Override
    public void processDeparture(Event event) {
        //Exit to the next event
        event.setCurrentModule(simulator.getTransactionalStorageModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);

    }
}
