package Interface;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.nio.file.Paths;


public class Interface extends Application {

    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception{
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
        primaryStage.show();
    }

    public void run(String[] args) {
        launch(args);
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}