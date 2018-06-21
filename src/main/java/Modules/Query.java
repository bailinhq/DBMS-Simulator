package main.java.Modules;

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



    public Query(QueryType queryType){
        type = queryType;
        setNumbersOfBlocks();
        setTimeOptimization();
        queryStatistics = new QueryStatistics();
    }

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

    private void setTimeOptimization(){
        switch (type){
            //Read Only
            case SELECT:
                timeOptimization = 0.1;
                break;
            case JOIN:
                timeOptimization = 0.1;
                break;

             // No read only
            case UPDATE:
                timeOptimization = 0.25;
                break;
            case DDL:
                timeOptimization = 0.25;
                break;
        }
    }


    int getPriority(){
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
        }
        return priority;
    }

    double getTimeOptimization() {
        return timeOptimization;
    }

    int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public QueryType getType() {
        return type;
    }

    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }
}
