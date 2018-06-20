package Controller;

import Statistics.SystemStatistics;
import Modules.Simulator;
import Interface.Interface;



public class Application {
    private Interface interfaceSimulator;
    private Simulator simulator;
    private int numberOfSimulations;
    private SystemStatistics systemStatistics;


    public Application(String args[]){
        simulator = new Simulator();
        interfaceSimulator = new Interface();
        systemStatistics = new SystemStatistics();
        interfaceSimulator.run(args);
    }

    public void setUp(Object parameters[]){
        //TODO hay que cambiar askParameters to getParameters cuando termines interfaz
        numberOfSimulations = (Integer)parameters[0];
        simulator.setParameters(parameters);
    }

    public void run(){
        int numberSimulation = 0;
        while(numberSimulation < this.numberOfSimulations) {
            simulator.simulate();
            //systemStatistics.addToList(simulator.getSimulationStatistics());
            ++numberSimulation;
        }
        //systemStatistics.generateSystemStatistics();
    }

    public void showWindow(){

    }
}
