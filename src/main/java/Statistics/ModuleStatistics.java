package main.java.Statistics;

import main.java.Event.QueryType;

public class ModuleStatistics {

    private int queueSize;
    private int totalQueueSize;

    private int numberOfSELECT;
    private int numberOfUPDATE;
    private int numberOfJOIN;
    private int numberOfDDL;

    private double timeOfSELECT;
    private double timeOfUPDATE;
    private double timeOfJOIN;
    private double timeOfDDL;

    /**
     * class constructor.
     */
    public ModuleStatistics() {
        this.queueSize = 0;
        this.totalQueueSize = 0;
        this.numberOfSELECT = 0;
        this.numberOfUPDATE = 0;
        this.numberOfJOIN = 0;
        this.numberOfDDL = 0;
        this.timeOfSELECT = 0.0;
        this.timeOfUPDATE = 0.0;
        this.timeOfJOIN = 0.0;
        this.timeOfDDL = 0.0;
    }

    /**
     * Methodo to increase the life time of the query, to later calculate the average life.
     * @param query Query type.
     * @param inputTime  Time the query entered to the module.
     * @param outputTime Time the query came out to the module.
     */
    public void increaseTimeOfQuery(QueryType query, double inputTime, double outputTime) {
        double timeTemp = outputTime - inputTime;
        switch (query){
            case UPDATE:
                timeOfUPDATE += timeTemp;
                break;
            case DDL:
                timeOfDDL += timeTemp;
                break;
            case JOIN:
                timeOfJOIN += timeTemp;
                break;
            case SELECT:
                timeOfSELECT += timeTemp;
                break;
        }
    }

    /**
     * Method to increase the number of queries processed in the module, they are increased by type to facilitate the calculation of averages.
     * @param query Query processed type.
     */
    public void increaseNumberOfQuery(QueryType query) {
        switch (query){
            case UPDATE:
                ++numberOfUPDATE;
                break;
            case DDL:
                ++numberOfDDL;
                break;
            case JOIN:
                ++numberOfJOIN;
                break;
            case SELECT:
                ++numberOfSELECT;
                break;
        }
    }

    /**
     * Method that calculates the average life of the type of query indicated.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getAverageTimeInModuleOfQuery(QueryType query) {
        double timeTemp = 0.0;
        switch (query){
            case UPDATE:
                timeTemp = timeOfUPDATE/numberOfUPDATE;
                break;
            case DDL:
                timeTemp = timeOfDDL/numberOfDDL;
                break;
            case JOIN:
                timeTemp = timeOfJOIN/numberOfJOIN;
                break;
            case SELECT:
                timeTemp = timeOfSELECT/numberOfSELECT;
                break;
        }
        return timeTemp;
    }

    /**
     * Method that calculates the average of queue length into the module.
     * @return Average of queue length into the module.
     */
    public double getAverageSizeQueue() {
        if(queueSize ==0)
            return 0;
        return (double)totalQueueSize/queueSize;
    }

    /**
     * Method to update the queue length into the module.
     * @param queueSize Queue length into the module.
     */
    public void increaseTotalQueueSize(int queueSize) {
        this.totalQueueSize += queueSize;
        ++this.queueSize;
    }

    /**
     * Method to calculates the number of queries processed in the module.
     * @return Number of queries processed in the module.
     */
    public int getTotalQueries(){ return this.numberOfDDL+this.numberOfSELECT+this.numberOfJOIN+this.numberOfUPDATE; }

    /**
     * Method to calculates the number of queries Select type processed in the module.
     * @return Number of queries Select type processed in the module.
     */
    public int getNumberOfSELECT() {
        return numberOfSELECT;
    }

    /**
     * Method to calculates the number of queries Update type processed in the module.
     * @return Number of queries Update type processed in the module.
     */
    public int getNumberOfUPDATE() {
        return numberOfUPDATE;
    }

    /**
     * Method to calculates the number of queries Join type processed in the module.
     * @return Number of queries Join type processed in the module.
     */
    public int getNumberOfJOIN() {
        return numberOfJOIN;
    }

    /**
     * Method to calculates the number of queries DDL type processed in the module.
     * @return Number of queries DDL type processed in the module.
     */
    public int getNumberOfDDL() {
        return numberOfDDL;
    }
}
