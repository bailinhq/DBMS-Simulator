package main.java.Statistics;

public class QueryStatistics {
    private double arrivalTime;
    private double arrivalTimeModule;
    private double departureTime;
    private double timeOfQuery;

    /**
     * Initializes an empty QueryStatistics.
     */
    public QueryStatistics(){
        this.arrivalTime = 0.0;
        this.arrivalTimeModule = 0.0;
        this.departureTime = 0.0;
        this.timeOfQuery = 0.0;
    }

    /**
     * Calculates the time taken from the arrival until departure and returns it.
     * @return Duration in the system.
     */
    public double getTimeInSystem(){
        if(departureTime-arrivalTime > 2 )
            System.out.println("Aca");
        return departureTime-arrivalTime;
    }

    /**
     * Returns the time of arrival.
     * @return time of arrival.
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the time of arrival.
     * @param arrivalTime time of arrival.
     */
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Returns the time of departure.
     * @return time of departure.
     */
    public double getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets the time of departure.
     * @param departureTime time of departure.
     */
    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getTimeOfQuery() {
        return timeOfQuery;
    }

    public void setTimeOfQuery(double timeOfQuery) {
        this.timeOfQuery = timeOfQuery;
    }

    /**
     * Returns the time of arrival in the Module.
     * @return time of arrival in the Module.
     */
    public double getArrivalTimeModule() {
        return arrivalTimeModule;
    }

    /**
     * Sets the time of arrival in the Module.
     * @param arrivalTimeModule time of arrival in the Module.
     */
    public void setArrivalTimeModule(double arrivalTimeModule) {
        this.arrivalTimeModule = arrivalTimeModule;
    }
}
