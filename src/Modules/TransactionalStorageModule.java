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
        //System.out.println("Llega cliente al modulo 4 -> "+event.getTimeClock());
        if(busyServers < numberServers){
            processClient(event);
            //System.out.println("Tiempo servicio -> "+event.getTimeClock()+"\n");
        }else{
            queue.offer(event);
        }
    }


    @Override
    public void processClient(Event event) {
        boolean processedEvent  = true;

        switch (event.getQuery().getType()){
            case DDL:
                if(busyServers > 0){
                    queue.offer(event);
                    processedEvent = false;
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
                    processedEvent = false;
                }

                break;

        }
        //Output is generated
        //If the event was processed, that is, it was not added to the module's queue.
        if(processedEvent) {
            event.setEventType(EventType.DEPARTURE);
            //event.setCurrentModule(this.simulator.getExecutorModule());
            this.simulator.addEvent(event);
        }
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
    public void processDeparture(Event event) {
        //System.out.println("Sale cliente al modulo 4 -> "+event.getTimeClock()+"\n\n");
        //Exit to the next event
        --busyServers;

        //Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());

        if(event.getQuery().getType() == QueryType.DDL)
            processingDDL = false;

        //event.setCurrentModule(simulator.getClientCommunicationsManagerModule());

        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getExecutorModule());
            event.setEventType(EventType.ARRIVAL);
            this.simulator.addEvent(event);
        }

        boolean noTimeOut = false;
        while (this.queue.size()>0 && !noTimeOut){
            Event temporal = this.queue.poll();
            if(!this.simulator.isTimeOut(event)){
                processClient(temporal);
                noTimeOut = true;
            }
        }
    }


}
