import Controller.Application;
import Modules.Simulator;

import java.util.Random;

public class Main {
    public static void main(String[] args){
        Simulator simulator = new Simulator();
        simulator.setMaxSimulationTime(60);
        simulator.setNumberOfSimulations(1);
        simulator.run();
    }
}
