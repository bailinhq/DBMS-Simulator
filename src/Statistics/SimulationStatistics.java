package Statistics;

public class SimulationStatistics {

    private int discardedNumberOfQuerys;
    private int numberOfQueries;
    private double timeLifeQueries;

    private ModuleStatistics clientModuleStatistics;
    private ModuleStatistics processModuleStatistics;
    private ModuleStatistics queryProcessorModuleStatistics;
    private ModuleStatistics transactionalStorageModuleStatistics;
    private ModuleStatistics executorModuleStatistics;


    public SimulationStatistics(){
        this.discardedNumberOfQuerys = 0;
    }

    public int getDiscardedNumberOfQueries() {
        return discardedNumberOfQuerys;
    }

    public void increaseDiscardedNumberOfQueries(){
        ++discardedNumberOfQuerys;
    }

    public double getTimeLifeOfQuery() {
        return timeLifeQueries/numberOfQueries;
    }

    public void increaseTimeLife( double inputTime, double outputTime){
        timeLifeQueries += outputTime-inputTime;
        ++numberOfQueries;
    }

}
