package main.java;

import main.java.Event.QueryType;
import main.java.Statistics.SystemStatistics;
import main.java.Interface.InterfaceController;

import static main.java.Simulator.*;


public class Application extends Thread {
    private InterfaceController interfaceController;
    private Simulator simulator;
    private int numberOfSimulations;
    private int numberOfSimulationsActual;
    private SystemStatistics systemStatistics;


    /**
     * class constructor
     * @param interfaceController Interface to control the application
     */
    public Application(InterfaceController interfaceController){
        this.interfaceController = interfaceController;
        simulator = new Simulator(interfaceController);
        systemStatistics = new SystemStatistics();
        numberOfSimulationsActual = 0;
    }

    /**
     * Method that starts the simulation once the parameters are inserted, also creates the simulation with the
     * indicated parameters.
     * @param parameters User parameters for the operation of the simulation (number of servers, times, among others).
     */
    public void setUp(Object parameters[]){
        numberOfSimulations = (Integer)parameters[0];
        simulator.setParameters(parameters);
        simulator.initialize();
    }



    /**
     * Method that controls the amount of simulation (parameter), also initializes data from one simulation to another
     * to have the independence of the information.
     * Also stores the statistics at the end of each simulation.
     * Once the simulation cycle is finished, it generates a general statistics.
     */
    @Override
    public void run(){
        while(numberOfSimulationsActual < this.numberOfSimulations) {

            simulator.initialize();

            this.interfaceController.updateSimulationNumber(this.numberOfSimulationsActual+1);
            simulator.simulate();
            this.simulator.updateAverageData();



            systemStatistics.addToList(simulator.getSimulationStatistics());
            ++numberOfSimulationsActual;
            try {
                this.interfaceController.semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        systemStatistics.generateSystemStatistics();

        this.updateAverageGlobalData();

        //Show buttons to see global statistics
        this.interfaceController.hideNextButton();
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the queue average sizes in the modules.
     */
    public double[] getGlobalAverageModulesQueueLength(){
        double queueLength[] = new double[5];
        queueLength[M_CLIENTS] = this.systemStatistics.getClientCommunicationsManagerQueueLength();
        queueLength[M_PROCESSES] = this.systemStatistics.getProcessManagerQueueLength();
        queueLength[M_QUERIES] = this.systemStatistics.getQueryProcessorQueueLength();
        queueLength[M_TRANSACTIONS] = this.systemStatistics.getTransactionalStorageQueueLength();
        queueLength[M_EXECUTIONS] = this.systemStatistics.getExecutorQueueLength();
        return queueLength;
    }
/*
    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getGlobalAverageTimeDDL(){
        double DDLNumber[] = new double[5];
        DDLNumber[M_CLIENTS] =      this.systemStatistics.getGlobalAverageTimeInClientModule(QueryType.DDL);
        DDLNumber[M_PROCESSES] =    this.systemStatistics.getGlobalAverageTimeInProcessModule(QueryType.DDL);
        DDLNumber[M_QUERIES] =      this.systemStatistics.getGlobalAverageTimeInQueriesModule(QueryType.DDL);
        DDLNumber[M_TRANSACTIONS] = this.systemStatistics.getGlobalAverageTimeInTransactionalModule(QueryType.DDL);
        DDLNumber[M_EXECUTIONS] =   this.systemStatistics.getGlobalAverageTimeInExecutorModule(QueryType.DDL);
        return DDLNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getGlobalAverageTimeUpdate(){
        double updateNumber[] = new double[5];
        updateNumber[M_CLIENTS] =      this.systemStatistics.getGlobalAverageTimeInClientModule(QueryType.UPDATE);
        updateNumber[M_PROCESSES] =    this.systemStatistics.getGlobalAverageTimeInProcessModule(QueryType.UPDATE);
        updateNumber[M_QUERIES] =      this.systemStatistics.getGlobalAverageTimeInQueriesModule(QueryType.UPDATE);
        updateNumber[M_TRANSACTIONS] = this.systemStatistics.getGlobalAverageTimeInTransactionalModule(QueryType.UPDATE);
        updateNumber[M_EXECUTIONS] =   this.systemStatistics.getGlobalAverageTimeInExecutorModule(QueryType.UPDATE);
        return updateNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getGlobalAverageTimeJoin(){
        double joinNumber[] = new double[5];
        joinNumber[M_CLIENTS] =      this.systemStatistics.getGlobalAverageTimeInClientModule(QueryType.JOIN);
        joinNumber[M_PROCESSES] =    this.systemStatistics.getGlobalAverageTimeInProcessModule(QueryType.JOIN);
        joinNumber[M_QUERIES] =      this.systemStatistics.getGlobalAverageTimeInQueriesModule(QueryType.JOIN);
        joinNumber[M_TRANSACTIONS] = this.systemStatistics.getGlobalAverageTimeInTransactionalModule(QueryType.JOIN);
        joinNumber[M_EXECUTIONS] =   this.systemStatistics.getGlobalAverageTimeInExecutorModule(QueryType.JOIN);
        return joinNumber;
    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    public double[] getGlobalAverageTimeSelect(){
        double selectNumber[] = new double[5];
        selectNumber[M_CLIENTS] =      this.systemStatistics.getGlobalAverageTimeInClientModule(QueryType.SELECT);
        selectNumber[M_PROCESSES] =    this.systemStatistics.getGlobalAverageTimeInProcessModule(QueryType.SELECT);
        selectNumber[M_QUERIES] =      this.systemStatistics.getGlobalAverageTimeInQueriesModule(QueryType.SELECT);
        selectNumber[M_TRANSACTIONS] = this.systemStatistics.getGlobalAverageTimeInTransactionalModule(QueryType.SELECT);
        selectNumber[M_EXECUTIONS] =   this.systemStatistics.getGlobalAverageTimeInExecutorModule(QueryType.SELECT);
        return selectNumber;
    }

    /**
     * Method that shows the average of the data of each run.
     */
    public void updateAverageGlobalData(){
        this.interfaceController.showGlobalAverageTimelifeQuery(this.systemStatistics.getTimeLifeQueries());
        this.interfaceController.showDDLGlobalAverageTime(this.getGlobalAverageTimeDDL());
        this.interfaceController.showJoinGlobalAverageTime(this.getGlobalAverageTimeJoin());
        this.interfaceController.showSelectGlobalAverageTime(this.getGlobalAverageTimeSelect());
        this.interfaceController.showUpdateGlobalAverageTime(this.getGlobalAverageTimeUpdate());
        this.interfaceController.showQueueGlobalAverageLength(this.getGlobalAverageModulesQueueLength());
        this.interfaceController.updateGlobalAverageTimeoutNumber(this.systemStatistics.getAverageTimeout());
        this.interfaceController.updateGlobalDiscarded(this.systemStatistics.getDiscardedNumberOfQueries());
        this.interfaceController.showConfidenceInterval(this.systemStatistics.getLowerConfidence(), this.systemStatistics.getHigherConfidence());
    }


}
