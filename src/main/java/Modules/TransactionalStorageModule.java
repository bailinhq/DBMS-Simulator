package main.java.Modules;
import java.util.*;

public class TransactionalStorageModule extends Module {

    private boolean processingDDL;
    private int queryDDL;
    Comparator<Event> compareAux;
    //private List<Event> queueTransactional;

    public  TransactionalStorageModule(Simulator simulator, RandomValueGenerator randSimulator, int numConcurrentProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numConcurrentProcesses;
        this.processingDDL = false;
        this.queryDDL = 0;
        compareAux = new ComparatorPriorityEvent();
        this.queue = new PriorityQueue<>(compareAux);
        //queueTransactional = new LinkedList<>();
    }



    @Override
    public void processArrival(Event event) {
        //main.java.Statistics
        event.getQuery().getQueryStatistics().setArrivalTimeModule(this.simulator.getClockTime());

        System.out.println("Llega cliente al modulo 4 -> "+event.getTimeClock() + "    " + event.getQuery().getType());
        if(busyServers < numberServers){
            processClient(event);
            //System.out.println("Tiempo servicio -> "+event.getTimeClock()+"\n");
        }else{
            queue.add(event);
            //queueTransactional.add(event);
        }

        //main.java.Statistics
        this.statisticsOfModule.increaseTotalQueueSize(this.queue.size());
    }


    @Override
    public void processClient(Event event) {
        boolean processedEvent  = true;

        switch (event.getQuery().getType()){
            case DDL:
                ++queryDDL;
                if(busyServers > 0){

                    queue.add(event);
                    //queueTransactional.add(event);
                    //Collections.sort(queueTransactional,compareAux);
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
                if(busyServers<numberServers && !processingDDL && queryDDL == 0){
                    ++busyServers;
                    event.setTimeClock(event.getTimeClock()+getServiceTime(event));
                }else {
                    if(queue.size()>5) {
                        System.out.println("HI");
                    }
                    queue.add(event);
                    //queueTransactional.add(event);
                   // Collections.sort(queueTransactional,compareAux);
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
        double timeTemp = numberServers*0.3;

        //time to load the blocks
        timeTemp += event.getQuery().getNumberOfBlocks()*(0.1/10);

        return timeTemp;
    }

    @Override
    public void processDeparture(Event event) {
        System.out.println("Sale cliente al modulo 4 -> "+event.getTimeClock()+"  " +event.getQuery().getType());
        //Exit to the next event
        --busyServers;

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());

        if(event.getQuery().getType() == QueryType.DDL) {
            --queryDDL;
            processingDDL = false;
        }

        //event.setCurrentModule(simulator.getClientCommunicationsManagerModule());

        if (!this.simulator.isTimeOut(event)) {
            //Exit to the next event
            event.setCurrentModule(simulator.getExecutorModule());
            event.setEventType(EventType.ARRIVAL);
            this.simulator.addEvent(event);
        }

        boolean noTimeOut = false;
        /*while (this.queue.size()>0 && !noTimeOut){
            Event temporal = this.queue.poll();
            if(!this.simulator.isTimeOut(event)){
                processClient(temporal);
                noTimeOut = true;
            }
        }*/

        //main.java.Statistics
        event.getQuery().getQueryStatistics().setDepartureTime(this.simulator.getClockTime());
        this.statisticsOfModule.increaseNumberOfQuery(event.getQuery().getType());
        this.statisticsOfModule.increaseTimeOfQuery(event.getQuery().getType(),event.getQuery().getQueryStatistics().getArrivalTimeModule(),this.simulator.getClockTime());
    }


}
