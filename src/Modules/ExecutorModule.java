package Modules;

import java.util.PriorityQueue;

public class ExecutorModule extends Module {


    ExecutorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
    }

    @Override
    public void processArrival(Event event) {
        if(busyServers < numberServers){
            processClient(event);
        }else{
            queue.offer(event);
        }
    }

    @Override
    public void processDeparture(Event event) {
        --busyServers;
        if(queue.size()>0)
        {
            Event temporal = queue.poll();
            processClient(event);
        }
        //Exit to the next event
        //event.setCurrentModule(simulator.getTransactionalStorageModule());
        event.setCurrentModule(simulator.getClientCommunicationsManagerModule());
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);

    }

    @Override
    public double getServiceTime(Event event) {
        double numBlocks = event.getQuery().getNumberOfBlocks();
        double timeTemp = numBlocks * numBlocks;
        switch (event.getQuery().getType()){
            case DDL:
                timeTemp += 0.5;
                break;
            case UPDATE:
                timeTemp += 1.0;
        }
        return timeTemp;
    }

    @Override
    public void processClient(Event event) {
        ++busyServers;
        event.setTimeClock(event.getTimeClock()+getServiceTime(event));
        //Output is generated
        //event.setCurrentModule(this.simulator.getExecutorModule());
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);
    }
}
