package main.java.Event;

import main.java.Modules.Module;

public class Event {
    private Query query;
    private EventType eventType;
    private Module currentModule;
    private double timeClock;

    /**
     * Initializes an Event with its query, time of the clock, type, and current module of the system.
     * @param query         Query of the event.
     * @param timeClock     Time of the event in the simulation clock.
     * @param eventType     Type of event.
     * @param currentModule Current module of the event.
     */
    public Event(Query query, double timeClock, EventType eventType, Module currentModule) {
        this.query = query;
        this.eventType = eventType;
        this.timeClock = timeClock;
        this.currentModule = currentModule;

    }

    /**
     * Returns the query of the event.
     * @return query.
     */
    public Query getQuery() {//    top priority
        return query;
    }

    /**
     * Sets the query of the event.
     * @param query
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * Returns the type of event.
     * @return event type.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Sets the event type.
     * @param eventType
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns the current module in which the event is located.
     * @return current module.
     */
    public Module getCurrentModule() {
        return currentModule;
    }

    /**
     * Sets the current module in which the event is located.
     * @param currentModule current module of the event.
     */
    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    /**
     * Returns the time of the clock.
     * @return time of the clock.
     */
    public double getTimeClock() {
        return timeClock;
    }

    /**
     * Sets the time of the clock.
     * @param timeClock current time clock of the event in the simulation.
     */
    public void setTimeClock(double timeClock) {
        this.timeClock = timeClock;
    }
}