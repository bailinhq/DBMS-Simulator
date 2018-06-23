package main.java.Statistics;

import main.java.Event.QueryType;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import java.util.ArrayList;

public class SystemStatistics {
    
    private static final int SELECT = 0;
    private static final int UPDATE = 1;
    private static final int JOIN   = 2;
    private static final int DDL    = 3;
    
    
    
    private ArrayList<SimulationStatistics> runsResults;
    private double discardedNumberOfQueries;
    private double timeLifeQueries;
    private double clientCommunicationsManagerQueueLength;
    private double processManagerQueueLength;
    private double queryProcessorQueueLength;
    private double transactionalStorageQueueLength;
    private double executorQueueLength;
    private double timeoutGlobal;

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
        timeoutGlobal = 0.0;

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
            timeoutGlobal += runsResult.getTimeoutQueries()*1.0;

            //Promedio de largo de cola por modulo
            clientCommunicationsManagerQueueLength += runsResult.getClientModuleStatistics().getAverageSizeQueue();
            processManagerQueueLength += runsResult.getProcessModuleStatistics().getAverageSizeQueue();
            queryProcessorQueueLength += runsResult.getQueryProcessorModuleStatistics().getAverageSizeQueue();
            transactionalStorageQueueLength += runsResult.getTransactionalStorageModuleStatistics().getAverageSizeQueue();
            executorQueueLength += runsResult.getExecutorModuleStatistics().getAverageSizeQueue();

            //Tiempos por sentencia por modulo
            clientCommunicationsManagerQueryTimes[SELECT] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            clientCommunicationsManagerQueryTimes[UPDATE] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            clientCommunicationsManagerQueryTimes[JOIN  ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            clientCommunicationsManagerQueryTimes[DDL   ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            processManagerQueryTimes[SELECT] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            processManagerQueryTimes[UPDATE] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            processManagerQueryTimes[JOIN  ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            processManagerQueryTimes[DDL   ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            queryProcessorQueryTimes[SELECT] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            queryProcessorQueryTimes[UPDATE] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            queryProcessorQueryTimes[JOIN  ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            queryProcessorQueryTimes[DDL   ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            transactionalStorageQueryTimes[SELECT] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            transactionalStorageQueryTimes[UPDATE] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            transactionalStorageQueryTimes[JOIN  ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            transactionalStorageQueryTimes[DDL   ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);

            executorQueryTimes[SELECT] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.SELECT);
            executorQueryTimes[UPDATE] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
            executorQueryTimes[JOIN  ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.JOIN);
            executorQueryTimes[DDL   ] += runsResult.getClientModuleStatistics().getAverageTimeInModuleOfQuery(QueryType.DDL);
        }

        discardedNumberOfQueries = discardedNumberOfQueries /simulationSize;
        timeLifeQueries = timeLifeQueries/simulationSize;
        timeoutGlobal = timeoutGlobal/simulationSize;
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
            TDistribution tDistribution;
            if(summaryStatistics.getN() == 1){
                tDistribution = new TDistribution(summaryStatistics.getN());
            } else{
                tDistribution = new TDistribution(summaryStatistics.getN() -1);
            }
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
     * Returns the mean timeout
     * @return the mean service time of a timeout
     */
    public double getAverageTimeout() {
        return timeoutGlobal;
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

    /**
     * Method that calculates the average life of the type of query indicated in client module.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getGlobalAverageTimeInClientModule(QueryType query) {
        double timeTemp = 0.0;
        timeTemp = getGlobalAverageTimeModules(query, timeTemp, clientCommunicationsManagerQueryTimes);
        return timeTemp;
    }

    /**
     * Method that calculates the average life of the type of query indicated in client module.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getGlobalAverageTimeInExecutorModule(QueryType query) {
        double timeTemp = 0.0;
        timeTemp = getGlobalAverageTimeModules(query, timeTemp, executorQueryTimes);
        return timeTemp;
    }

    /**
     * Method that calculates the average life of the type of query indicated in process module.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getGlobalAverageTimeInProcessModule(QueryType query) {
        double timeTemp = 0.0;
        timeTemp = getGlobalAverageTimeModules(query, timeTemp, processManagerQueryTimes);
        return timeTemp;
    }

    /**
     * Method that returns the average lifetime of a query in a specific module
     * @param query type of query
     * @param timeTemp variable to return the average
     * @param processManagerQueryTimes Array with data of each module
     * @return global average of time in seconds
     */
    private double getGlobalAverageTimeModules(QueryType query, double timeTemp, double[] processManagerQueryTimes) {
        switch (query){
            case SELECT:
                timeTemp = processManagerQueryTimes[SELECT];
                break;
            case UPDATE:
                timeTemp = processManagerQueryTimes[UPDATE];
                break;
            case JOIN:
                timeTemp = processManagerQueryTimes[JOIN];
                break;
            case DDL:
                timeTemp = processManagerQueryTimes[DDL];
                break;
        }
        return timeTemp;
    }

    /**
     * Method that calculates the average life of the type of query indicated in transactional module.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getGlobalAverageTimeInTransactionalModule(QueryType query) {
        double timeTemp = 0.0;
        timeTemp = getGlobalAverageTimeModules(query, timeTemp, transactionalStorageQueryTimes);
        return timeTemp;
    }

    /**
     * Method that calculates the average life of the type of query indicated in transactional module.
     * @param query Type of query of which the average life is wanted.
     * @return Average life of the type of query.
     */
    public double getGlobalAverageTimeInQueriesModule(QueryType query) {
        double timeTemp = 0.0;
        timeTemp = getGlobalAverageTimeModules(query, timeTemp, queryProcessorQueryTimes);
        return timeTemp;
    }
}
