package Statistics;

public class SimulationStatistics {

    private int discardedNumberOfQuerys;
    private int numberOfQuerys;
    private double timeLifeOfQuery;

    private ModuleStatistics clientModuleStatistics;
    private ModuleStatistics processModuleStatistics;
    private ModuleStatistics queryProcessorModuleStatistics;
    private ModuleStatistics transactionalStorageModuleStatistics;
    private ModuleStatistics executorModuleStatistics;



    public int getDiscardedNumberOfQueries() {
        return discardedNumberOfQuerys;
    }

    public void increaseDiscardedNumberOfQueries(){
        ++discardedNumberOfQuerys;
    }

    public double getTimeLifeOfQuery() {

        return 1.0;
    }

}
