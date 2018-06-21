package main.java.Controller;

import main.java.Statistics.SystemStatistics;
import main.java.Modules.Simulator;
import main.java.Interface.InterfaceController;


public class Application {
    private InterfaceController interfaceController;
    private Simulator simulator;
    private int numberOfSimulations;
    private int numberOfSimulationsActual;
    private SystemStatistics systemStatistics;


    public Application(InterfaceController interfaceController){
        this.interfaceController = interfaceController;
        simulator = new Simulator(interfaceController);
        systemStatistics = new SystemStatistics();
        numberOfSimulationsActual = 0;
    }



    public void setUp(Object parameters[]){
        numberOfSimulations = (Integer)parameters[0];
        simulator.setParameters(parameters);
    }

    public void run(){
        while(numberOfSimulationsActual < this.numberOfSimulations) {
            this.interfaceController.updateSimulationNumber(this.numberOfSimulationsActual+1);
            simulator.initialize();
            simulator.simulate();
            systemStatistics.addToList(simulator.getSimulationStatistics());
            ++numberOfSimulationsActual;
        }
        systemStatistics.generateSystemStatistics();
    }

}
