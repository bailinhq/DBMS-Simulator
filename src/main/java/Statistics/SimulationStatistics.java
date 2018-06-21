package main.java.Statistics;

public class SimulationStatistics {

    private int discardedNumberOfQuerys;
    private int numberOfQueries;
    private double timeLifeQueries;

    private ModuleStatistics clientModuleStatistics;
    private ModuleStatistics processModuleStatistics;
    private ModuleStatistics queryProcessorModuleStatistics;
    private ModuleStatistics transactionalStorageModuleStatistics;
    private ModuleStatistics executorModuleStatistics;

    public SimulationStatistics(ModuleStatistics aclientModuleStatistics,
                                ModuleStatistics aprocessModuleStatistics,
                                ModuleStatistics aqueryProcessorModuleStatistics,
                                ModuleStatistics atransactionalStorageModuleStatistics,
                                ModuleStatistics aexecutorModuleStatistics){

        clientModuleStatistics = aclientModuleStatistics;
        processModuleStatistics = aprocessModuleStatistics;
        queryProcessorModuleStatistics = aqueryProcessorModuleStatistics;
        transactionalStorageModuleStatistics = atransactionalStorageModuleStatistics;
        executorModuleStatistics = aexecutorModuleStatistics;
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

    public ModuleStatistics getClientModuleStatistics() {
        return clientModuleStatistics;
    }

    public ModuleStatistics getProcessModuleStatistics() {
        return processModuleStatistics;
    }

    public ModuleStatistics getQueryProcessorModuleStatistics() {
        return queryProcessorModuleStatistics;
    }

    public ModuleStatistics getTransactionalStorageModuleStatistics() {
        return transactionalStorageModuleStatistics;
    }

    public ModuleStatistics getExecutorModuleStatistics() {
        return executorModuleStatistics;
    }
}
