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

    //order: SELECT, UPDATE, JOIN, DDL
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

        clientCommunicationsManagerQueryTimes = new double[4];
        processManagerQueryTimes = new double[4];
        queryProcessorQueryTimes = new double[4];
        transactionalStorageQueryTimes = new double[4];
        executorQueryTimes = new double[4];
    }

    public void addToList(SimulationStatistics simulationStatistics){
        runsResults.add(simulationStatistics);
    }

    public void generateSystemStatistics(){
        int simulationSize = runsResults.size();
        for (SimulationStatistics runsResult : runsResults) {
            discardedNumberOfQuerys += runsResult.getDiscardedNumberOfQueries();
            timeLifeQueries += runsResult.getTimeLifeOfQuery();

            //Promedio de largo de cola por modulo
            clientCommunicationsManagerQueueLength += runsResult.getClientModuleStatistics().getAverageSizeQueue();
            processmanagerQueueLength += runsResult.getProcessModuleStatistics().getAverageSizeQueue();
            queryProcessorQueueLength += runsResult.getQueryProcessorModuleStatistics().getAverageSizeQueue();
            transactionalStorageQueueLength += runsResult.getTransactionalStorageModuleStatistics().getAverageSizeQueue();
            executorQueueLength += runsResult.getExecutorModuleStatistics().getAverageSizeQueue();

            //Tiempos por sentencia por modulo
            clientCommunicationsManagerQueryTimes[0] += runsResult.getClientModuleStatistics().getTimeOfSELECT();
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfUPDATE();
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfJOIN();
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfDDL();

            processManagerQueryTimes[0] += runsResult.getClientModuleStatistics().getTimeOfSELECT();
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfUPDATE();
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfJOIN();
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfDDL();

            queryProcessorQueryTimes[0] += runsResult.getClientModuleStatistics().getTimeOfSELECT();
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfUPDATE();
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfJOIN();
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfDDL();

            transactionalStorageQueryTimes[0] += runsResult.getClientModuleStatistics().getTimeOfSELECT();
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfUPDATE();
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfJOIN();
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfDDL();

            executorQueryTimes[0] += runsResult.getClientModuleStatistics().getTimeOfSELECT();
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfUPDATE();
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfJOIN();
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getTimeOfDDL();
        }

        discardedNumberOfQuerys = discardedNumberOfQuerys/simulationSize;
        timeLifeQueries = timeLifeQueries/(double) simulationSize;
        clientCommunicationsManagerQueueLength = clientCommunicationsManagerQueueLength/(double) simulationSize;
        processmanagerQueueLength = processmanagerQueueLength/(double) simulationSize;
        queryProcessorQueueLength = queryProcessorQueueLength/(double) simulationSize;
        transactionalStorageQueueLength = transactionalStorageQueueLength/(double) simulationSize;
        executorQueueLength = executorQueueLength/(double) simulationSize;

        for (int i = 0; i < 4; i++){
            clientCommunicationsManagerQueryTimes[i] = clientCommunicationsManagerQueryTimes[i]/(double) simulationSize;
            processManagerQueryTimes[i] = processManagerQueryTimes[i]/(double) simulationSize;
            queryProcessorQueryTimes[i] = queryProcessorQueryTimes[i]/(double) simulationSize;
            transactionalStorageQueryTimes[i] = transactionalStorageQueryTimes[i]/(double) simulationSize;
            executorQueryTimes[i] = executorQueryTimes[i]/ (double) simulationSize;
        }
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

    public double[] getClientCommunicationsManagerQueryTimes() {
        return clientCommunicationsManagerQueryTimes;
    }

    public double[] getProcessManagerQueryTimes() {
        return processManagerQueryTimes;
    }

    public double[] getQueryProcessorQueryTimes() {
        return queryProcessorQueryTimes;
    }

    public double[] getTransactionalStorageQueryTimes() {
        return transactionalStorageQueryTimes;
    }

    public double[] getExecutorQueryTimes() {
        return executorQueryTimes;
    }
}
