package Statistics;

public class QueryStatistics {
    private double arrivalTime;
    private double departureTime;
    private double timeOfQuery;

    public QueryStatistics(){
        arrivalTime = 0;
        departureTime = 0;
        timeOfQuery = 0;
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


}
