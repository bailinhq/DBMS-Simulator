package Modules;

import java.util.Comparator;

public class ComparatorPriorityEvent implements Comparator<Event> {

    private ComparatorNormalEvent comparatorNormalEvent = new ComparatorNormalEvent();

    @Override
    public int compare(Event a, Event b) {
        //System.out.println("\n\n\nEntra a comparar Trans\n\n\n");
        if(a.getQuery().getPriority() == b.getQuery().getPriority()){
            //return comparatorNormalEvent.compare(a,b);
            return 1;
            //return 1;
        }else if(a.getQuery().getPriority() < b.getQuery().getPriority()){
            return -1;
        }else{
            return 1;
        }
    }
}
