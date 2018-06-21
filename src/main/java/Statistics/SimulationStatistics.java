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

    /**
     * class constructor.
     * @param aClientModuleStatistics               Statistics of the connections module
     * @param aProcessModuleStatistics              Statistics of the process module
     * @param aQueryProcessorModuleStatistics       Statistics of the query process module
     * @param aTransactionalStorageModuleStatistics Statistics of the transactional module
     * @param aExecutorModuleStatistics             Statistics of the executor module
     */
    public SimulationStatistics(ModuleStatistics aClientModuleStatistics,
                                ModuleStatistics aProcessModuleStatistics,
                                ModuleStatistics aQueryProcessorModuleStatistics,
                                ModuleStatistics aTransactionalStorageModuleStatistics,
                                ModuleStatistics aExecutorModuleStatistics) {

        clientModuleStatistics = aClientModuleStatistics;
        processModuleStatistics = aProcessModuleStatistics;
        queryProcessorModuleStatistics = aQueryProcessorModuleStatistics;
        transactionalStorageModuleStatistics = aTransactionalStorageModuleStatistics;
        executorModuleStatistics = aExecutorModuleStatistics;
        this.discardedNumberOfQuerys = 0;
    }

    /**
     * Method to access the current number of discarded queries.
     *
     * @return Number of discarded queries.
     */
    public int getDiscardedNumberOfQueries() {
        return discardedNumberOfQuerys;
    }

    /**
     * Method to update the current number of discarded queries.
     */
    public void increaseDiscardedNumberOfQueries() {
        ++discardedNumberOfQuerys;
    }

    /**
     * Method to access the average lifetime of a query
     * @return Average lifetime of a query.
     */
    public double getTimeLifeOfQuery() {
        return timeLifeQueries / numberOfQueries;
    }

    /**
     * Method to update the data to calculate the average lifetime of a query.
     * @param inputTime  Time the query entered.
     * @param outputTime Time the query came out.
     */
    public void increaseTimeLife(double inputTime, double outputTime) {
        timeLifeQueries += outputTime - inputTime;
        ++numberOfQueries;
    }


    /**
     * Method to access the statistics of the connections module.
     * @return Statistics of the connections module.
     */
    public ModuleStatistics getClientModuleStatistics() {
        return clientModuleStatistics;
    }

    /**
     * Method to access the statistics of the process module.
     * @return Statistics of the process module.
     */
    public ModuleStatistics getProcessModuleStatistics() {
        return processModuleStatistics;
    }

    /**
     * Method to access the statistics of the query process module.
     * @return Statistics of the query process module.
     */
    public ModuleStatistics getQueryProcessorModuleStatistics() {
        return queryProcessorModuleStatistics;
    }

    /**
     * Method to access the statistics of the transactional module.
     * @return Statistics of the transactional module.
     */
    public ModuleStatistics getTransactionalStorageModuleStatistics() {
        return transactionalStorageModuleStatistics;
    }

    /**
     * Method to access the statistics of the executor module.
     * @return Statistics of the executor module.
     */
    public ModuleStatistics getExecutorModuleStatistics() {
        return executorModuleStatistics;
    }
}
