package Modules;

public class Event implements Comparable<Event> {
    private Query query;
    private EventType eventType;
    private Module currentModule;
    private double timeClock;


    public Event(Query aQuery, double time, EventType aEventType, Module theCurrentModule) {
        query = aQuery;
        eventType = aEventType;
        timeClock = time;
        currentModule = theCurrentModule;
    }

    @Override
    public int compareTo(Event o) {
        return -1;
    }


    public Query getQuery() {
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