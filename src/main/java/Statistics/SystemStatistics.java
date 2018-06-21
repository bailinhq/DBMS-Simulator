package main.java.Statistics;

import main.java.Event.QueryType;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import java.util.ArrayList;

public class SystemStatistics {
    private ArrayList<SimulationStatistics> runsResults;
    private double discardedNumberOfQueries;
    private double timeLifeQueries;
    private double clientCommunicationsManagerQueueLength;
    private double processManagerQueueLength;
    private double queryProcessorQueueLength;
    private double transactionalStorageQueueLength;
    private double executorQueueLength;

    private ArrayList<Double> timeLifeQueriesConfidenceInterval;
    private double lowerConfidence;
    private double higherConfidence;

    //order: SELECT, UPDATE, JOIN, DDL
    private double[] clientCommunicationsManagerQueryTimes;
    private double[] processManagerQueryTimes;
    private double[] queryProcessorQueryTimes;
    private double[] transactionalStorageQueryTimes;
    private double[] executorQueryTimes;

    public SystemStatistics(){
        this.runsResults = new ArrayList<>();
        discardedNumberOfQueries = 0.0;
        timeLifeQueries = 0.0;
        clientCommunicationsManagerQueueLength = 0.0;
        processManagerQueueLength = 0.0;
        queryProcessorQueueLength = 0.0;
        transactionalStorageQueueLength = 0.0;
        executorQueueLength = 0.0;

        clientCommunicationsManagerQueryTimes = new double[4];
        processManagerQueryTimes = new double[4];
        queryProcessorQueryTimes = new double[4];
        transactionalStorageQueryTimes = new double[4];
        executorQueryTimes = new double[4];

        timeLifeQueriesConfidenceInterval = new ArrayList<>();
        lowerConfidence = 0.0;
        higherConfidence = 0.0;
    }

    public void addToList(SimulationStatistics simulationStatistics){
        runsResults.add(simulationStatistics);
    }

    public void generateSystemStatistics(){
        double simulationSize = (double) runsResults.size();
        for (SimulationStatistics runsResult : runsResults) {
            discardedNumberOfQueries += runsResult.getDiscardedNumberOfQueries();
            timeLifeQueries += runsResult.getTimeLifeOfQuery();
            timeLifeQueriesConfidenceInterval.add(runsResult.getTimeLifeOfQuery());

            //Promedio de largo de cola por modulo
            clientCommunicationsManagerQueueLength += runsResult.getClientModuleStatistics().getAverageSizeQueue();
            processManagerQueueLength += runsResult.getProcessModuleStatistics().getAverageSizeQueue();
            queryProcessorQueueLength += runsResult.getQueryProcessorModuleStatistics().getAverageSizeQueue();
            transactionalStorageQueueLength += runsResult.getTransactionalStorageModuleStatistics().getAverageSizeQueue();
            executorQueueLength += runsResult.getExecutorModuleStatistics().getAverageSizeQueue();

            //Tiempos por sentencia por modulo
            clientCommunicationsManagerQueryTimes[0] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            clientCommunicationsManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            processManagerQueryTimes[0] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            processManagerQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            queryProcessorQueryTimes[0] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            queryProcessorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            transactionalStorageQueryTimes[0] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            transactionalStorageQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            executorQueryTimes[0] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            executorQueryTimes[1] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);
        }

        discardedNumberOfQueries = discardedNumberOfQueries /simulationSize;
        timeLifeQueries = timeLifeQueries/simulationSize;
        clientCommunicationsManagerQueueLength = clientCommunicationsManagerQueueLength/simulationSize;
        processManagerQueueLength = processManagerQueueLength /simulationSize;
        queryProcessorQueueLength = queryProcessorQueueLength/simulationSize;
        transactionalStorageQueueLength = transactionalStorageQueueLength/simulationSize;
        executorQueueLength = executorQueueLength/simulationSize;

        for (int i = 0; i < 4; i++){
            clientCommunicationsManagerQueryTimes[i] = clientCommunicationsManagerQueryTimes[i]/simulationSize;
            processManagerQueryTimes[i] = processManagerQueryTimes[i]/simulationSize;
            queryProcessorQueryTimes[i] = queryProcessorQueryTimes[i]/simulationSize;
            transactionalStorageQueryTimes[i] = transactionalStorageQueryTimes[i]/simulationSize;
            executorQueryTimes[i] = executorQueryTimes[i]/simulationSize;
        }
        this.calculateConfidenceInterval();
    }

    private void calculateConfidenceInterval(){
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        double criticalValue = 0.0;
        for (double value : timeLifeQueriesConfidenceInterval){
            summaryStatistics.addValue(value);
        }
        try{
            TDistribution tDistribution = new TDistribution(summaryStatistics.getN() -1);
            //0.95 confidence interval
            criticalValue = tDistribution.inverseCumulativeProbability(1.0 - (1 - 0.95)/2);
            criticalValue = criticalValue * summaryStatistics.getStandardDeviation()/Math.sqrt(summaryStatistics.getN());
        } catch (MathIllegalArgumentException e){
            criticalValue = Double.NaN;
        }
        lowerConfidence = summaryStatistics.getMean() - criticalValue;
        higherConfidence = summaryStatistics.getMean() + criticalValue;
    }

    public double getDiscardedNumberOfQueries() {
        return discardedNumberOfQueries;
    }

    public double getTimeLifeQueries() {
        return timeLifeQueries;
    }

    public double getClientCommunicationsManagerQueueLength() {
        return clientCommunicationsManagerQueueLength;
    }

    public double getProcessManagerQueueLength() {
        return processManagerQueueLength;
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

    public double getLowerConfidence() {
        return lowerConfidence;
    }

    public double getHigherConfidence() {
        return higherConfidence;
    }
}
