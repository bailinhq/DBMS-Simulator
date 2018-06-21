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

    /**
     * Constructs a SystemStatistics and its fields.
     */
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

    /**
     * Adds a SimulationStatistics to the ArrayList runsResults.
     * @param simulationStatistics
     */
    public void addToList(SimulationStatistics simulationStatistics){
        runsResults.add(simulationStatistics);
    }

    /**
     * Generates the system statistics and updates the field variables.
     * System statistics are the mean discarded number of total queries, life expectancy of queries, queue length for each
     * module (ClientCommunicationsManager, ProcessManager, QueryProcessor, TransactionalStorage, and Executor) and
     * the time of service for each type of query (SELECT, JOIN, UPDATE, DDL) for each module, and a 95% confidence
     * interval of the mean life expectancy of queries.
     */
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

    /**
     * Calculates the lower bound and higher bound of a 95% confidence interval from the values of the mean
     * life expectancy of a query in the system.
     */
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

    /**
     * Returns the mean number of discarded queries of the system statistics.
     * @return mean the number of discarded queries.
     */
    public double getDiscardedNumberOfQueries() {
        return discardedNumberOfQueries;
    }

    /**
     * Returns the mean life expectancy of a query in the system statistics.
     * @return mean life expectancy of a query.
     */
    public double getTimeLifeQueries() {
        return timeLifeQueries;
    }

    /**
     * Returns the mean queue length of the ClientCommunicationsManager module of the system statistics.
     * @return the mean queue length of the ClientCommunicationsManager module
     */
    public double getClientCommunicationsManagerQueueLength() {
        return clientCommunicationsManagerQueueLength;
    }

    /**
     * Returns the mean queue length of the ProcessManager module of the system statistics.
     * @return the mean queue length of the ProcessManager module
     */
    public double getProcessManagerQueueLength() {
        return processManagerQueueLength;
    }

    /**
     * Returns the mean queue length of the QueryProcessor module of the system statistics.
     * @return the mean queue length of the QueryProcessor module
     */
    public double getQueryProcessorQueueLength() {
        return queryProcessorQueueLength;
    }

    /**
     * Returns the mean queue length of the TransactionalStorage module of the system statistics.
     * @return the mean queue length of the TransactionalStorage module
     */
    public double getTransactionalStorageQueueLength() {
        return transactionalStorageQueueLength;
    }

    /**
     * Returns the mean queue length of the Executor module of the system statistics.
     * @return the mean queue length of the Executor module
     */
    public double getExecutorQueueLength() {
        return executorQueueLength;
    }

    /**
     * Returns the mean service time of a query in the ClientCommunicationsManager module of the system statistics.
     * @return the mean service time of a query in the ClientCommunicationsManager module
     */
    public double[] getClientCommunicationsManagerQueryTimes() {
        return clientCommunicationsManagerQueryTimes;
    }

    /**
     * Returns the mean service time of a query in the ProcessManager module of the system statistics.
     * @return the mean service time of a query in the ProcessManager module
     */
    public double[] getProcessManagerQueryTimes() {
        return processManagerQueryTimes;
    }

    /**
     * Returns the mean service time of a query in the QueryProcessor module of the system statistics.
     * @return the mean service time of a query in the QueryProcessor module
     */
    public double[] getQueryProcessorQueryTimes() {
        return queryProcessorQueryTimes;
    }

    /**
     * Returns the mean service time of a query in the TransactionalStorage module of the system statistics.
     * @return the mean service time of a query in the TransactionalStorage module
     */
    public double[] getTransactionalStorageQueryTimes() {
        return transactionalStorageQueryTimes;
    }

    /**
     * Returns the mean service time of a query in the Executor module of the system statistics.
     * @return the mean service time of a query in the Executor module
     */
    public double[] getExecutorQueryTimes() {
        return executorQueryTimes;
    }

    /**
     * Returns the lower bound from the confidence interval from the mean life expectancy of a query in the system.
     * @return the lower bound of the confidence interval.
     */
    public double getLowerConfidence() {
        return lowerConfidence;
    }


    /**
     * Returns the higher bound from the confidence interval from the mean life expectancy of a query in the system.
     * @return the higher bound of the confidence interval.
     */
    public double getHigherConfidence() {
        return higherConfidence;
    }
}
