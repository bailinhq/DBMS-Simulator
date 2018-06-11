package Modules;

public class Query {
    // Uniform distribution range for select type queries
    private static final int a = 1;
    private static final int b = 64;

    //public int type; //1: SELECT - 2: UPDATE - 3:JOIN - 4:DDL
    private double timeOptimization;
    private QueryType type;
    private int numberOfBlocks;



    public Query(QueryType queryType){
        type = queryType;
        setNumbersOfBlocks();
        setTimeOptimization();
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

    public double getTimeOptimization() {
        return timeOptimization;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public QueryType getType() {
        return type;
    }

}
