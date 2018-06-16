package Modules;

public class Event implements Comparable<Event> {
    private Query query;
    private EventType eventType;
    private Module currentModule;
    private double timeClock;


    public Event(Query query, double timeClock, EventType eventType, Module currentModule) {
        this.query = query;
        this.eventType = eventType;
        this.timeClock = timeClock;
        this.currentModule = currentModule;

    }

    @Override
    public int compareTo(Event o) {
        //If the current module is different from the transaction module
        return compareEvents(o);
        /*if(o.getCurrentModule().getCurrentModuleType() != 4) {
            return compareEvents(o);
        }else{
            if(this.getQuery().getPriority() == o.getQuery().getPriority()){
                return compareEvents(o);
            }else if(this.getQuery().getPriority() < o.getQuery().getPriority()){
                return -1;
            }else{
                return 1;
            }
        }*/
    }

    //compare events by time and type
    public int compareEvents(Event o){
        if (this.getTimeClock() < o.getTimeClock()) {
            return -1;
        } else if (this.getTimeClock() > o.getTimeClock()) {
            return 1;
        } else {
            if (this.getEventType() == EventType.DEPARTURE) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public Query getQuery() {//    top priority
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Module getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    public double getTimeClock() {
        return timeClock;
    }

    public void setTimeClock(double timeClock) {
        this.timeClock = timeClock;
    }


}