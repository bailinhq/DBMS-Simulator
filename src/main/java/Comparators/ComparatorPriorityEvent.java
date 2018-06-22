package main.java.Comparators;

import main.java.Event.Event;

import java.util.Comparator;

public class ComparatorPriorityEvent implements Comparator<Event> {

    /**
     * Comparator for the queue of the transactions module where priority is given, DDL, Update, Join and Select.
     * @param event1 Insert event
     * @param event2 Event on the list (compare)
     * @return Position indicator
     */
    @Override
    public int compare(Event event1, Event event2) {
        if(event1.getQuery().getPriority() == event2.getQuery().getPriority()){
            return 1;
        }else if(event1.getQuery().getPriority() < event2.getQuery().getPriority()){
            return -1;
        }else{
            return 1;
        }
    }
}
