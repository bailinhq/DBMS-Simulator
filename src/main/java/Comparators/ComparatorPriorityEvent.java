package main.java.Comparators;

import main.java.Event.Event;

import java.util.Comparator;

public class ComparatorPriorityEvent implements Comparator<Event> {

    @Override
    public int compare(Event a, Event b) {
        if(a.getQuery().getPriority() == b.getQuery().getPriority()){
            return 1;
        }else if(a.getQuery().getPriority() < b.getQuery().getPriority()){
            return -1;
        }else{
            return 1;
        }
    }
}
