package main.java.Comparators;

import main.java.Event.Event;
import main.java.Event.EventType;

import java.util.Comparator;

public class ComparatorNormalEvent implements Comparator<Event> {

    @Override
    public int compare(Event a, Event b){
        if (a.getTimeClock() < b.getTimeClock()) {
            return -1;
        } else if (a.getTimeClock() > b.getTimeClock()) {
            return 1;
        } else {
            if (a.getEventType() == EventType.DEPARTURE) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
