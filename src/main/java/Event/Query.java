package main.java.Event;

import main.java.RandomValueGenerator;
import main.java.Statistics.QueryStatistics;

public class Query {
    // Uniform distribution range for select type queries
    private static final int a = 1;
    private static final int b = 64;

    //public int type; //1: SELECT - 2: UPDATE - 3:JOIN - 4:DDL
    private double timeOptimization;
    private QueryType type;
    private int numberOfBlocks;
    private QueryStatistics queryStatistics;

    /**
     * Initializes a Query with a QuerType.
     * @param queryType
     */
    public Query(QueryType queryType){
        type = queryType;
        setNumbersOfBlocks();
        setTimeOptimization();
        queryStatistics = new QueryStatistics();
    }

    /**
     * Sets the number of blocks for each query to be used in the simulation.
     */
    private void setNumbersOfBlocks(){
        switch (type){
            case JOIN:
                //To generate random values.
                RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
                numberOfBlocks = randomValueGenerator.generateUniformDistributionValue(a,b);
                break;
            case SELECT:
                numberOfBlocks = 1;
                break;
            case DDL:
            case UPDATE:
                    numberOfBlocks = 0;
                    break;
        }
    }

    /**
     * Sets the time optimization for each type of query.
     */
    private void setTimeOptimization(){
        switch (type){
            //Read Only
            case SELECT:
            case JOIN:
                timeOptimization = 0.1;
                break;

             // No read only
            case UPDATE:
            case DDL:
                timeOptimization = 0.25;
                break;
        }
    }

    /**
     * Returns the priority depending of the QueryType.
     * @return priority.
     */
    public int getPriority(){
        //top priority 1
        int priority = 0;
        switch (this.type){
            case DDL:
                priority = 1;
                break;
            case UPDATE:
                priority = 2;
                break;
            case JOIN:
                priority = 3;
                break;
            case SELECT:
                priority = 4;
                break;
        }
        return priority;
    }

    /**
     * Returns the Time Optimization.
     * @return Time Optimization.
     */
    public double getTimeOptimization() {
        return timeOptimization;
    }

    /**
     * Returns the number of blocks.
     * @return number of blocks.
     */
    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    /**
     * Returns the type of query.
     * @return type of query.
     */
    public QueryType getType() {
        return type;
    }

    /**
     * Returns the QueryStatistics.
     * @return QueryStatistics
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }
}
