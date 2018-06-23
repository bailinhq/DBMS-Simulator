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
                System.err.println("Application : error waiting for signal");
            }
        }
        systemStatistics.generateSystemStatistics();

        this.updateAverageGlobalData();

        //Show buttons to see global statistics
        this.interfaceController.hideNextButton();


        System.out.println("Voy a esperar");
        System.out.println("Voy a morir");

    }

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the queue average sizes in the modules.
     */
    public double[] getAverageModulesQueueLength(){
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
    /*public double[] getAverageTimeDDL(){
        double DDLNumber[] = new double[5];
        DDLNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        DDLNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.DDL);
        return DDLNumber;
    }*/

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    /*public double[] getAverageTimeUpdate(){
        double updateNumber[] = new double[5];
        updateNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        updateNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.UPDATE);
        return updateNumber;
    }*/

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    /*public double[] getAverageTimeJoin(){
        double joinNumber[] = new double[5];
        joinNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        joinNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.JOIN);
        return joinNumber;
    }*/

    /**
     * method that saves and returns in a vector the average life time of each type of query in each module.
     * @return Array with the average life time of each type of query in each module.
     */
    /*public double[] getAverageTimeSelect(){
        double selectNumber[] = new double[5];
        selectNumber[M_CLIENTS] = this.clientCommunicationsManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_PROCESSES] = this.processManagerModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_QUERIES] = this.queryProcessorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_TRANSACTIONS] = this.transactionalStorageModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        selectNumber[M_EXECUTIONS] = this.executorModule.getStatisticsOfModule().getAverageTimeInModuleOfQuery(QueryType.SELECT);
        return selectNumber;
    }*/

    /**
     * Method that shows the average of the data of each run.
     */
    public void updateAverageGlobalData(){
        //this.interfaceController.showAverageLifetimeQuery(this.clockTime);
        //this.interfaceController.showDDLAverageTime(this.getAverageTimeDDL());
        //this.interfaceController.showJoinAverageTime(this.getAverageTimeJoin());
        //this.interfaceController.showSelectAverageTime(this.getAverageTimeSelect());
        //this.interfaceController.showUpdateAverageTime(this.getAverageTimeUpdate());
        this.interfaceController.showQueueGlobalAverageLength(this.getAverageModulesQueueLength());
        //this.interfaceController.updateTimeoutNumber(timeoutNumber);
    }


}
