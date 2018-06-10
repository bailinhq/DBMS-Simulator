package Modules;

import java.util.PriorityQueue;

public class ClientCommunicationsManagerModule extends Module {


    ClientCommunicationsManagerModule(Simulator simulator, RandomValueGenerator randSimulator, int numConnections) {
        super(simulator, randSimulator);
        this.numberServers = numConnections;
    }


    @Override
    public void processArrival(Event event) {
        if(this.busyServers<numberServers){
            event.setCurrentModule(simulator.getProcessManagerModule());
            event.setEventType(EventType.ARRIVAL);
            this.simulator.addEvent(event);
        }else
        {
            this.simulator.increaseRejectQueries();
        }
    }

    @Override
    public void processDeparture(Event event) {

    }
}
