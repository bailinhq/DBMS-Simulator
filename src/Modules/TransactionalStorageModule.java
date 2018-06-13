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
        if(queue.size()>0){
            Event temporal =  queue.poll();
            this.processClient(temporal);
        }
        //event.setCurrentModule(simulator.getClientCommunicationsManagerModule());
        event.setCurrentModule(simulator.getExecutorModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);



    }

    @Override
    public double getServiceTime(Event event) {
        //coordination time
        double timeTemp = busyServers*0.3;

        //time to load the blocks
        timeTemp += event.getQuery().getNumberOfBlocks()*(0.1/10);

        return timeTemp;
    }

    @Override
    public void processClient(Event event) {
        boolean processedEvent  = false;

        switch (event.getQuery().getType()){
            case DDL:
                if(busyServers > 0){
                    queue.offer(event);
                    processedEvent = true;
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
                    processedEvent = true;
                }

                break;

        }
        //Output is generated
        if(!processedEvent) {
            event.setEventType(EventType.DEPARTURE);
            //event.setCurrentModule(this.simulator.getExecutorModule());
            this.simulator.addEvent(event);
        }
    }
}
