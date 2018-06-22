package main.java;

import main.java.Statistics.SystemStatistics;
import main.java.Interface.InterfaceController;


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
            this.interfaceController.updateSimulationNumber(this.numberOfSimulationsActual+1);
            simulator.initialize();
            simulator.simulate();
            systemStatistics.addToList(simulator.getSimulationStatistics());
            ++numberOfSimulationsActual;
        }
        systemStatistics.generateSystemStatistics();
        System.out.println("Voy a esperar");
        System.out.println("Voy a morir");

    }


}
