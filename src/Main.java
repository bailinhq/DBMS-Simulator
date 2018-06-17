import Controller.Application;
import Modules.*;
import Statistics.SimulationStatistics;
import javafx.scene.layout.Priority;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args){
        /*Comparator<Event> compareAux = new ComparatorFIFO();
        PriorityQueue<Event> priorityQueue = new PriorityQueue<>(compareAux);
        Simulator simulator = new Simulator();

        Query query = new Query(QueryType.DDL);
        Event a = new Event(query,0,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(a);

        query = new Query(QueryType.JOIN);
        Event b = new Event(query,1,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(b);

        query = new Query(QueryType.SELECT);
        Event c = new Event(query,2,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(c);

        query = new Query(QueryType.UPDATE);
        Event d = new Event(query,3,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(d);

        query = new Query(QueryType.DDL);
        Event e = new Event(query,4,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(e);

        query = new Query(QueryType.UPDATE);
        Event f = new Event(query,5,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(f);


        query = new Query(QueryType.UPDATE);
        Event g = new Event(query,6,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(g);

        query = new Query(QueryType.DDL);
        Event h = new Event(query,7,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(h);

        query = new Query(QueryType.SELECT);
        Event i = new Event(query,8,EventType.DEPARTURE,new TransactionalStorageModule(simulator,simulator.getValueGenerator(),10));
        priorityQueue.offer(i);

        Iterator<Event> iterator = priorityQueue.iterator();
        while (iterator.hasNext())
        {
            if(iterator.next().getQuery().getType() == QueryType.DDL){
                iterator.remove();
            }
        }
        simulator.checkTimeOutQueue(priorityQueue);
        int x = priorityQueue.size();
        for (int j = 0; j < x; j++) {
            Event temporal = priorityQueue.poll();
            System.out.println(temporal.getQuery().getType());
        }
        */
        Simulator simulator = new Simulator();
        simulator.setMaxSimulationTime(60);
        simulator.setNumberOfSimulations(1);
        simulator.run();
    }
}
