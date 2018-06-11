package Modules;

import java.util.PriorityQueue;

public class TransactionalStorageModule extends Module {

    private boolean processingDDL;

    TransactionalStorageModule(Simulator simulator, RandomValueGenerator randSimulator, int numConcurrentProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numConcurrentProcesses;
        this.processingDDL = false;
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
        //Exit to the next event
        --busyServers;
        if(event.getQuery().getType() == QueryType.DDL)
            processingDDL = false;
        //transmission time R = numbers of blocks
        event.setTimeClock(event.getTimeClock()+event.getQuery().getNumberOfBlocks());

        event.setCurrentModule(simulator.getClientCommunicationsManagerModule());
        event.setEventType(EventType.DEPARTURE);
        this.simulator.addEvent(event);

    }

    @Override
    public double getServiceTime(Event event) {
        double timeTemp = busyServers*0.3;
        switch (event.getQuery().getType())
        {
            case JOIN:
            case SELECT:
            case UPDATE:
                timeTemp += event.getQuery().getNumberOfBlocks()*(0.1/10);
                break;
            case DDL:
                timeTemp = 0.3;
                break;
        }
        return timeTemp;
    }

    @Override
    public void processClient(Event event) {
        switch (event.getQuery().getType()){
            case DDL:
                if(busyServers >0){
                    queue.offer(event);
                }else{
                    ++busyServers;
                    processingDDL = true;
                    event.setTimeClock(event.getTimeClock()+getServiceTime(event));
                }

                break;
            case UPDATE:
            case SELECT:
            case JOIN:
                if(busyServers<numberServers && !processingDDL){
                    ++busyServers;
                    event.setTimeClock(event.getTimeClock()+getServiceTime(event));
                }else {
                    queue.offer(event);
                }

                break;

        }
        //Output is generated
        event.setCurrentModule(this.simulator.getExecutorModule());
        this.simulator.addEvent(event);
    }
}
