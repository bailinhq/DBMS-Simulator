package Statistics;

import java.util.ArrayList;

public class SystemStatistics {
    private ArrayList<SimulationStatistics> runsResults;
    private int discardedNumberOfQuerys;
    private double timeLifeQueries;
    private double clientCommunicationsManagerQueueLength;
    private double processmanagerQueueLength;
    private double queryProcessorQueueLength;
    private double transactionalStorageQueueLength;
    private double executorQueueLength;

    private double[] clientCommunicationsManagerQueryTimes;
    private double[] processManagerQueryTimes;
    private double[] queryProcessorQueryTimes;
    private double[] transactionalStorageQueryTimes;
    private double[] executorQueryTimes;

    public SystemStatistics(){
        this.runsResults = new ArrayList<>();
        discardedNumberOfQuerys = 0;
        timeLifeQueries = 0;
        clientCommunicationsManagerQueueLength = 0;
        processmanagerQueueLength = 0;
        queryProcessorQueueLength = 0;
        transactionalStorageQueueLength = 0;
        executorQueueLength = 0;
    }

    public void addToList(SimulationStatistics simulationStatistics){
        runsResults.add(simulationStatistics);
    }

    public void generateSystemStatistics(){
        int simulationSize = runsResults.size();
        for (SimulationStatistics runsResult : runsResults) {
            discardedNumberOfQuerys += runsResult.getDiscardedNumberOfQueries();
            timeLifeQueries += runsResult.getTimeLifeOfQuery();
            clientCommunicationsManagerQueueLength += runsResult.getClientModuleStatistics().getAverageSizeQueue();
            processmanagerQueueLength += runsResult.getProcessModuleStatistics().getAverageSizeQueue();
            queryProcessorQueueLength += runsResult.getQueryProcessorModuleStatistics().getAverageSizeQueue();
            transactionalStorageQueueLength += runsResult.getTransactionalStorageModuleStatistics().getAverageSizeQueue();
            executorQueueLength += runsResult.getExecutorModuleStatistics().getAverageSizeQueue();
        }
        discardedNumberOfQuerys = discardedNumberOfQuerys/simulationSize;
        timeLifeQueries = timeLifeQueries/(double) simulationSize;
        clientCommunicationsManagerQueueLength = clientCommunicationsManagerQueueLength/(double) simulationSize;
        processmanagerQueueLength = processmanagerQueueLength/(double) simulationSize;
        queryProcessorQueueLength = queryProcessorQueueLength/(double) simulationSize;
        transactionalStorageQueueLength = transactionalStorageQueueLength/(double) simulationSize;
        executorQueueLength = executorQueueLength/(double) simulationSize;
    }

    public int getDiscardedNumberOfQuerys() {
        return discardedNumberOfQuerys;
    }

    public double getTimeLifeQueries() {
        return timeLifeQueries;
    }

    public double getClientCommunicationsManagerQueueLength() {
        return clientCommunicationsManagerQueueLength;
    }

    public double getProcessmanagerQueueLength() {
        return processmanagerQueueLength;
    }

    public double getQueryProcessorQueueLength() {
        return queryProcessorQueueLength;
    }

    public double getTransactionalStorageQueueLength() {
        return transactionalStorageQueueLength;
    }

    public double getExecutorQueueLength() {
        return executorQueueLength;
    }
}
