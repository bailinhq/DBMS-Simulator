public class ModuleStatistics {

    private int numberOfSELECT;
    private int numberOfUPDATE;
    private int numberOfJOIN;
    private int numberOfDDL;

    private double timeOfSELECT;
    private double timeOfUPDATE;
    private double timeOfJOIN;
    private double timeOfDDL;

    public QueryType type;

    public ModuleStatistics(){

    }

    public void increaseTimeOfQuery(QueryType query, double inputTime, double outputTime) {

    }

    public void increaseNumberOfQuery(QueryType query) {

    }

    public double setAverageTimeInModuleOfQuery(QueryType query) {

        return 1.0;
    }


}
