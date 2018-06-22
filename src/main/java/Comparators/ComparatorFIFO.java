package main.java.Comparators;

import main.java.Event.Event;

import java.util.Comparator;

public class ComparatorFIFO implements Comparator<Event> {

    /**
     * Method to sort the queues of the FIFO modes
     * @param event Insert event
     * @param event2 Event on the list (compare)
     * @return Position indicator
     */
    @Override
    public int compare(Event event, Event event2) {
        if(event.getTimeClock()<event2.getTimeClock()){
            return -1;
        }
        return 1;
    }
}
