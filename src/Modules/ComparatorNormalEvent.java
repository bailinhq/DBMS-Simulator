package Modules;

import java.util.Comparator;

public class ComparatorNormalEvent implements Comparator<Event> {

    @Override
    public int compare(Event a, Event b){
        //System.out.println("///Entra a comparar otro///");
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
