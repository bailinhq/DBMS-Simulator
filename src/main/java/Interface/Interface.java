package main.java.Interface;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Interface extends Application {

    private double xOffset;
    private double yOffset;
    InterfaceController interfaceController;

    /**
     * class constructor
     */
    public Interface(){
         this.interfaceController = new InterfaceController();
    }



    /**
     * Method invoked automatically when the application is launched from the main
     * @param primaryStage Scenario or main container of the graphical interface
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //load the fxml of the graphical interface
        Parent root = FXMLLoader.load(getClass().getResource("SimulatorInterface.fxml"));

        /*Method to move application*/
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX()-xOffset);
                primaryStage.setY(event.getScreenY()-yOffset);
            }
        });


        //Transparent window frame
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Simulator");

        //Transparent background
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        //show graphical interface
        primaryStage.show();
    }

    /**
     * main method
     * @param args parameters passed by console
     */
    public static void main(String[] args){
        launch(args);
    }
}