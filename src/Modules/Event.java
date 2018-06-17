package Modules;

public class Event {
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