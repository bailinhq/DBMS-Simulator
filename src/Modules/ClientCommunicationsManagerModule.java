package Modules;

import java.util.PriorityQueue;

public class ClientCommunicationsManagerModule extends Module {


    ClientCommunicationsManagerModule(Simulator simulator) {
        super(simulator);
    }

    @Override
    public void processArrival(Event event) {
        if(this.busyServers<numberServers){
            event.setEventType(EventType.DEPARTURE);
        }else
        {
            //se rechazan
        }
    }

    @Override
    public void processDeparture(Event event) {
        event.setCurrentModule(simulator.getProcessManagerModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }
}
