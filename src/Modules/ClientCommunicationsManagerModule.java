package Modules;

import java.util.PriorityQueue;

public class ClientCommunicationsManagerModule extends Module {

    //Receive k connections
    ClientCommunicationsManagerModule(Simulator simulator, RandomValueGenerator randSimulator, int numConnections) {
        super(simulator, randSimulator);
        this.numberServers = numConnections;
    }


    @Override
    public void processArrival(Event event) {
        if(this.busyServers < numberServers){
            ++busyServers;
            processClient(event);
        }else
        {
            //this.simulator.increaseRejectQueries();
        }
        //A new arrival is generated
        this.simulator.generateNewEvent();
    }

    @Override
    public void processDeparture(Event event) {
        --busyServers;
    }

    @Override
    public double getServiceTime(Event event) {
        return 0.0;
    }

    @Override
    public void processClient(Event event) {
        event.setCurrentModule(simulator.getProcessManagerModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }
}
