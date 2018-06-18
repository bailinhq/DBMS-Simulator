package Interface;
import Controller.Application;
import Modules.*;
import javafx.stage.Stage;

public class Interface extends javafx.application.Application {
    private Controller.Application application;
    public Interface(Controller.Application anApplication){
        application = anApplication;
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public void showMenu(){

    }
    public void showStatistics(){

    }

    public void showRealTime(Event event){

    }

    public Object[] askParameters(){
        Object[] parameters = new Object[8];
        return parameters;
    }


}