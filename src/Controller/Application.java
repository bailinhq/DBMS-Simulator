package Controller;

import Interface.Interface;
import Modules.Simulator;
import Statistics.SystemStatistics;

public class Application {
   // private Interface anInterface;
    private Simulator simulator;
    private int numberOfSimulations;
    private SystemStatistics systemStatistics;
    public Application(){
        this.setUp();
        this.showWindow();
    }

    public void setUp(){
        //anInterface = new Interface(this);
        //TODO hay que cambiar askParameters to getParameters cuando termines interfaz
        //simulator = new Simulator(anInterface.askParameters());
        systemStatistics = new SystemStatistics();
        //numberOfSimulations = (Integer) anInterface.askParameters()[0];
    }

    public void run(){
        int numberSimulation = 0;
        while(numberSimulation < this.numberOfSimulations) {
            simulator.simulate();
            systemStatistics.addToList(simulator.getSimulationStatistics());
            ++numberSimulation;
        }
        systemStatistics.generateSystemStatistics();
    }

    public void showWindow(){

    }
}
