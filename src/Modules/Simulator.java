package Modules;

import Statistics.SimulationStatistics;

import java.util.PriorityQueue;
import java.util.Random;

public class Simulator {
    private RandomValueGenerator valueGenerator;
    private ClientCommunicationsManagerModule clientCommunicationsManagerModule;
    private ProcessManagerModule processManagerModule;
    private QueryProcessorModule queryProcessorModule;
    private TransactionalStorageModule transactionalStorageModule;
    private ExecutorModule executorModule;
    private PriorityQueue<Event> queue;
    public int numClientes;
    public int llegan;

    private int numberOfSimulations;
    private double maxSimulationTime;
    private boolean delay;
    private int maxConcurrentConnectionsSystem;
    private int maxConcurrentConnectionsQueries;
    private int maxQueriesInTransactions;
    private int maxQueriesInExecutor;
    private double timeout;
    private double clockTime;
    private boolean firstEvent;

    private SimulationStatistics simulationStatistics;
    private Random randomGenerator;

    public Simulator(){
        this.valueGenerator = new RandomValueGenerator();

        this.clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this,valueGenerator,10);
        this.processManagerModule = new ProcessManagerModule(this,valueGenerator);
        this.queryProcessorModule = new QueryProcessorModule(this,valueGenerator,5);
        this.executorModule = new ExecutorModule(this,valueGenerator,5);
        this.simulationStatistics = new SimulationStatistics();
        this.queue = new PriorityQueue<>(new ComparatorNormalEvent());
        this.transactionalStorageModule =  new TransactionalStorageModule(this,valueGenerator,5);
        this.timeout =10;
        this.firstEvent = true;
    }

    public Simulator(Object parameters[]){
        this.setUp(parameters);
    }

    public void setUp(Object parameters[]){
        //TODO cambiar los valores que reciben los modulos
        randomGenerator = new Random(0);
        valueGenerator = new RandomValueGenerator();
        clientCommunicationsManagerModule = new ClientCommunicationsManagerModule(this, valueGenerator, 0);
        processManagerModule = new ProcessManagerModule(this, valueGenerator);
        queryProcessorModule = new QueryProcessorModule(this, valueGenerator , 0);
        transactionalStorageModule = new TransactionalStorageModule(this, valueGenerator, 0);
        executorModule = new ExecutorModule(this, valueGenerator, 0);

        queue = new PriorityQueue<>(new ComparatorNormalEvent());
        this.setParameters(parameters);
    }

    public void generateNewEvent(){
        Query query = generateQuery();


        double timeTemp = 0;
        if(!firstEvent){
            //30 connections per minute -> 1/lambda = 30 connections/min -> 1/lambda = 0.5connections/sec -> lambda = 2
            timeTemp = valueGenerator.generateExponentialDistributionValue(0.5 );

        }else {
            firstEvent = false;
        }

        queue.offer(new Event(query, clockTime+timeTemp, EventType.ARRIVAL, clientCommunicationsManagerModule));

        //System.out.println("Tipo->" + query.getType()+"\n\n");
    }



    private Query generateQuery(){
        randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
        Query query;
        query = new Query(QueryType.DDL);
        /*if (random <= 0.30){
            query = new Query(QueryType.SELECT);
        } else if (random <= 0.55){
            query = new Query(QueryType.UPDATE);
        } else if (random <= 0.90){
            query = new Query(QueryType.JOIN);
        } else {
            query = new Query(QueryType.DDL);
        }*/
        return query;
    }




    private void setParameters(Object parameters[]){
        numberOfSimulations = (Integer) parameters[0];
        maxSimulationTime = (Double) parameters[1];
        delay = (Boolean) parameters[2];
        maxConcurrentConnectionsSystem = (Integer) parameters[3];
        maxConcurrentConnectionsQueries = (Integer) parameters[4];
        maxQueriesInTransactions = (Integer) parameters[5];
        maxQueriesInExecutor = (Integer) parameters[6];
        timeout = (Double) parameters[7];
    }

    void addEvent(Event event){
        queue.offer(event);
    }



    public boolean isTimeOut(Event event){
        if(event.getQuery().getQueryStatistics().getTimeInSystem()> this.timeout){
            System.out.println("\033[31mHay timeout"+event.getQuery().getQueryStatistics().getTimeInSystem());
            return true;
        }else{
            return false;
        }
    }


    public void run(){
        int numberSimulation = 0;
        while(numberSimulation < this.numberOfSimulations) {
            simulate();
            ++numberSimulation;
        }
    }

    private void simulate(){
        generateNewEvent();
        numClientes = 0;
        llegan = 0;
        while (this.clockTime <= this.maxSimulationTime){
            Event event = queue.poll();
            if(event!=null){
                this.clockTime = event.getTimeClock();
                event.getCurrentModule().processEvent(event);
            }else{
                clockTime = maxSimulationTime+1;
            }
        }
        System.out.println("Clientes atendidos " + numClientes + "\n Rechazados " + this.simulationStatistics.getDiscardedNumberOfQueries()+"\nLlega "+ llegan);
        System.out.println("El total de consultas atendidas en Modulo Clientes fue "+ this.clientCommunicationsManagerModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio en Modulo Clientes fue "+ this.clientCommunicationsManagerModule.statisticsOfModule.getAverageSizeQueue());

        System.out.println("\n\nEl total de consultas atendidas en Modulo Procesos fue "+ this.processManagerModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Procesos fue "+ this.processManagerModule.statisticsOfModule.getAverageSizeQueue());

        System.out.println("\n\nEl total de consultas atendidas en Modulo Consultas fue "+ this.queryProcessorModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Procesos fue "+ this.queryProcessorModule.statisticsOfModule.getAverageSizeQueue());

        System.out.println("\n\nEl total de consultas atendidas en Modulo Transacciones fue "+ this.transactionalStorageModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Procesos fue "+ this.transactionalStorageModule.statisticsOfModule.getAverageSizeQueue());

        System.out.println("\n\nEl total de consultas atendidas en Modulo Exe fue "+ this.executorModule.statisticsOfModule.getTotalQueries());
        System.out.println("El tamanio de cola promedio  en Modulo Procesos fue "+ this.executorModule.statisticsOfModule.getAverageSizeQueue());

        System.out.println("FIN");
    }




    private RandomValueGenerator getValueGenerator() {
        return valueGenerator;
    }

    ClientCommunicationsManagerModule getClientCommunicationsManagerModule() {
        return clientCommunicationsManagerModule;
    }

    ProcessManagerModule getProcessManagerModule() {
        return processManagerModule;
    }

    QueryProcessorModule getQueryProcessorModule() {
        return queryProcessorModule;
    }

    TransactionalStorageModule getTransactionalStorageModule() {
        return transactionalStorageModule;
    }

    ExecutorModule getExecutorModule() {
        return executorModule;
    }

    public double getClockTime() {
        return clockTime;
    }

    public  void increaseRejectQueries(){
        this.simulationStatistics.increaseDiscardedNumberOfQueries();
    }

    public void setNumberOfSimulations(int numberOfSimulations) {
        this.numberOfSimulations = numberOfSimulations;
    }

    public void setMaxSimulationTime(double maxSimulationTime) {
        this.maxSimulationTime = maxSimulationTime;
    }
}
