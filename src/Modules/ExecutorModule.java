package Modules;

import java.util.PriorityQueue;

public class ExecutorModule extends Module {


    ExecutorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
    }

    @Override
    public void processArrival(Event event) {
        System.out.println("Llega cliente al modulo 5");
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
            processClient(temporal);
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

        //B^2 milliseconds = (B^2)/1000 seconds
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
        }
        //transmission time R = numbers of blocks
        timeTemp += event.getQuery().getNumberOfBlocks();

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
