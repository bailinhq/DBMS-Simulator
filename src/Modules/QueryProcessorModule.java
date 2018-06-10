package Modules;

import java.util.PriorityQueue;

public class QueryProcessorModule extends Module {

    QueryProcessorModule(Simulator simulator, RandomValueGenerator randSimulator, int numProcesses) {
        super(simulator, randSimulator);
        this.numberServers = numProcesses;
    }

    @Override
    public void processArrival(Event event) {
        if(busyServers>0){
            queue.offer(event);
        }else{
            ++busyServers;
            event.setTimeClock(simulator.getClockTime()+getProcessTimeQuery(event));

            //Output is generated
            event.setEventType(EventType.DEPARTURE);
            this.simulator.addEvent(event);
        }
    }

    @Override
    public void processDeparture(Event event) {
        //Exit to the next event
        event.setCurrentModule(simulator.getTransactionalStorageModule());
        event.setEventType(EventType.ARRIVAL);
        this.simulator.addEvent(event);

    }

    private double getProcessTimeQuery(Event event){
        double processingTime = 0.0;
        //lexical validation
        processingTime += 0.1;

        //syntactic validation --> uniform random value 0<=x<=1 seconds
        processingTime += this.randomValueGenerator.generateUniformDistributionValue(0,1);

        //semantic validation --> uniform random value 0<=x<=2 seconds
        processingTime += this.randomValueGenerator.generateUniformDistributionValue(0,2);

        //permit verification --> exponential random value with mean 0.7 seconds --> lambda = 1/0.7
        processingTime+= this.randomValueGenerator.generateExponentialDistributionValue(1.0/0.7);

        //TODO definir si realmente la consulta debe saber cuanto dura para que la optimizen.
        //query optimization
        processingTime += event.getQuery().getTimeOptimization();

        return  processingTime;


    }
}
