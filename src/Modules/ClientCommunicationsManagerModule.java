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
        System.out.println("Llega cliente al modulo 1 -> "+event.getTimeClock());
        if(this.busyServers < numberServers){
            processClient(event);
            //System.out.println("Tiempo servicio -> "+event.getTimeClock()+"\n");
        }else
        {
            this.simulator.increaseRejectQueries();
        }
        //A new arrival is generated
        this.simulator.generateNewEvent();
    }

    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setCurrentModule(simulator.getProcessManagerModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);
    }

    @Override
    public double getServiceTime(Event event) {
        return 0.0;
    }



    @Override
    public void processDeparture(Event event) {
        System.out.println("\n\nCliente atendido"+ event.getTimeClock()+"\n");
        --busyServers;
        this.simulator.generateNewEvent();
    }


}
