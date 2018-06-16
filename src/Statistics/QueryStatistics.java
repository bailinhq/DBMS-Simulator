package Statistics;

public class QueryStatistics {
    private double arrivalTime;
    private double arrivalTimeModule;
    private double departureTime;
    private double timeOfQuery;

    public QueryStatistics(){
        this.arrivalTime = 0;
        this.arrivalTimeModule = 0;
        this.departureTime = 0;
        this.timeOfQuery = 0;
    }

    public double getTimeInSystem(){
        return departureTime-arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getTimeOfQuery() {
        return timeOfQuery;
    }

    public void setTimeOfQuery(double timeOfQuery) {
        this.timeOfQuery = timeOfQuery;
    }

    public double getArrivalTimeModule() {
        return arrivalTimeModule;
    }

    public void setArrivalTimeModule(double arrivalTimeModule) {
        this.arrivalTimeModule = arrivalTimeModule;
    }
}
