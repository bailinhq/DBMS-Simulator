package Controller;

import Interface.Interface;
import Modules.Simulator;

public class Application {
    private Interface anInterface;
    private Simulator simulator;
    public Application(){
        this.setUp();
    }

    public void setUp(){
        anInterface = new Interface(this);
    }

    public void showWindow(){

    }
}
