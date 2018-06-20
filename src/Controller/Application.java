package Controller;

import Statistics.SystemStatistics;
import Modules.Simulator;
import Interface.InterfaceController;



public class Application {
    private InterfaceController interfaceController;
    private Simulator simulator;
    private int numberOfSimulations;
    private SystemStatistics systemStatistics;


    public Application(InterfaceController interfaceController){
        this.interfaceController = interfaceController;
        simulator = new Simulator();
        systemStatistics = new SystemStatistics();

        System.out.println("Sigo en la aplicacion");
    }



    public void setUp(Object parameters[]){
        //TODO hay que cambiar askParameters to getParameters cuando termines interfaz
        numberOfSimulations = (Integer)parameters[0];
        simulator.setParameters(parameters);
    }

    public void run2(){
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
