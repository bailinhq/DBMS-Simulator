package main.java.Comparators;

import main.java.Event.Event;
import main.java.Event.EventType;

import java.util.Comparator;

public class ComparatorNormalEvent implements Comparator<Event> {

    /**
     * Comparator to order the list of simulation events, where it is first ordered by time, then arrivals and last departures.
     * @param event1 Insert event
     * @param event2 Event on the list (compare)
     * @return Position indicator
     */
    @Override
    public int compare(Event event1, Event event2){
        if (event1.getTimeClock() < event2.getTimeClock()) {
            return -1;
        } else if (event1.getTimeClock() > event2.getTimeClock()) {
            return 1;
        } else {
            if (event1.getEventType() == EventType.DEPARTURE) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
