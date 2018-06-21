package main.java.Interface;

import main.java.Application;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;

import static main.java.Modules.Simulator.*;

public class InterfaceController implements Initializable{
    Application application;

    //constant values
    private static final int NUMBER_SIMULATION = 0;
    private static final int MAX_SIMULATION_TIME = 1;
    private static final int DELAY = 2;
    private static final int K = 3;
    private static final int N = 4;
    private static final int P = 5;
    private static final int M = 6;
    private static final int T = 7;

    //Graphic interface elements
    //arrows
    @FXML private ImageView welcomeArrow;
    @FXML private ImageView simulatorArrow;
    @FXML private ImageView systemArrow;
    @FXML private ImageView runArrow;
    @FXML private ImageView statsArrow;
    @FXML private ImageView imgbuttonSimulator;
    @FXML private ImageView imgButtonSystem;

    //Anchor panes
    @FXML private AnchorPane welcomePanel;
    @FXML private AnchorPane simulatorPanel;
    @FXML private AnchorPane systemPanel;
    @FXML private AnchorPane runPanel;
    @FXML private AnchorPane statsPanel;

    //Text Field
    @FXML private JFXTextField numberSimulationsText;
    @FXML private JFXTextField simulationTimeText;
    @FXML private JFXTextField timeoutTxt;

    @FXML private JFXTextField kText;
    @FXML private JFXTextField nText;
    @FXML private JFXTextField pText;
    @FXML private JFXTextField mText;
    @FXML private JFXTextField tText;

    //delay
    @FXML private JFXToggleButton delayToggle;


    @FXML private JFXTextField clockTxt;
    @FXML private JFXTextField discardedTxt;
    @FXML private JFXTextField simulationNumberTxt;

    //Statistics
    //number of servers
    @FXML private JFXTextField cServers;
    @FXML private JFXTextField pServers;
    @FXML private JFXTextField qServers;
    @FXML private JFXTextField tServers;
    @FXML private JFXTextField eServers;

    //queue length
    @FXML private JFXTextField cQueue;
    @FXML private JFXTextField pQueue;
    @FXML private JFXTextField qQueue;
    @FXML private JFXTextField tQueue;
    @FXML private JFXTextField eQueue;

    //DDL
    @FXML private JFXTextField cDDL;
    @FXML private JFXTextField pDDL;
    @FXML private JFXTextField qDDL;
    @FXML private JFXTextField tDDL;
    @FXML private JFXTextField eDDL;

    //DDL
    @FXML private JFXTextField cUpdate;
    @FXML private JFXTextField pUpdate;
    @FXML private JFXTextField qUpdate;
    @FXML private JFXTextField tUpdate;
    @FXML private JFXTextField eUpdate;

    //DDL
    @FXML private JFXTextField cJoin;
    @FXML private JFXTextField pJoin;
    @FXML private JFXTextField qJoin;
    @FXML private JFXTextField tJoin;
    @FXML private JFXTextField eJoin;

    //DDL
    @FXML private JFXTextField cSelect;
    @FXML private JFXTextField pSelect;
    @FXML private JFXTextField qSelect;
    @FXML private JFXTextField tSelect;
    @FXML private JFXTextField eSelect;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideAll();
        //Show welcome panel
        welcomePanel.setVisible(true);
        welcomeArrow.setVisible(true);

        //Validators are added to text fields
        //Only allow positive integers
        numberSimulationsText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        kText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        nText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        pText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        mText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);

        //When the following fields are modified a series of validations is performed.
        simulationTimeText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    simulationTimeText.setText(oldValue);
                }
            }
        });

        //When the following fields are modified a series of validations is performed.
        tText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    tText.setText(oldValue);
                }
            }
        });



    }

    /**
     * Method to close the application or graphic interface.
     * @param mouseEvent Mouse click event
     */
    public void onExitButtonClicked(MouseEvent mouseEvent){
        Platform.exit();
        System.exit(0);
    }

    /**
     * Method to show the settings panel, by pressing the continue
     * button in the welcome panel.
     * @param mouseEvent Mouse click event
     */
    public void onWelcomeContinueButtonClicked(MouseEvent mouseEvent){
        hideAll();

        setDisabledSideButtons(false);

        simulatorArrow.setVisible(true);
        simulatorPanel.setVisible(true);
    }

    /**
     * Method to show the panel of system configurations, by pressing the
     * continue button in the simulator settings panel.
     * @param mouseEvent Mouse click event
     */
    public void onSettingSimulatorContinueButtonClicked(MouseEvent mouseEvent){
        hideAll();

        systemPanel.setVisible(true);
        systemArrow.setVisible(true);

    }

    /**
     * Method to run the simulations, by clicking on the run button.
     * @param mouseEvent Mouse click event
     */
    public void onSettingSystemRunButtonClicked(MouseEvent mouseEvent){
        //If there are no empty fields.
        if(validateFields()) {
            hideAll();
            //Disables buttons on the side panel of the graphical interface.
            setDisabledSideButtons(true);
            //It shows an arrow symbol
            runArrow.setVisible(true);
            //Show the running panel
            runPanel.setVisible(true);
            if ( application == null  )
            {
                application = new Application(this);
            }
            Object parameters[] = parametersToArray();
            //It shows in the interface the configuration of servers
            // given by the user.
            showNumberServers(parameters);
            //The simulation is configured.
            application.setUp(parameters);
            //The simulation starts running
            application.run();
        }else{
            System.out.print("Faltan cosas");
        }
    }

    /**
     * Hide all the graphical interface panels
     */
    private void hideAll(){
        welcomeArrow.setVisible(false);
        welcomePanel.setVisible(false);

        simulatorArrow.setVisible(false);
        simulatorPanel.setVisible(false);

        systemPanel.setVisible(false);
        systemArrow.setVisible(false);

        runArrow.setVisible(false);
        runPanel.setVisible(false);

        statsArrow.setVisible(false);
        statsPanel.setVisible(false);
    }


    /**
     * Validator of whole numbers.
     */
    EventHandler<KeyEvent> handlerWholeNumbers = new EventHandler<KeyEvent>() {
        private boolean isValid =false;
        @Override
        public void handle(KeyEvent event) {
            if(isValid)
                event.consume();
            if(!event.getCode().toString().matches("[0-9]") && event.getCode() != KeyCode.BACK_SPACE
                    && !event.getCode().isDigitKey()){
                if (event.getEventType() == KeyEvent.KEY_PRESSED)
                    isValid = true;
                else if( event.getEventType() == KeyEvent.KEY_RELEASED)
                    isValid = false;
            }
        }
    };

    /**
     *
     * @param disabled If true, disable the buttons on the side panel,
     *                 if you do not activate them.
     */
    private void setDisabledSideButtons(boolean disabled){
        imgbuttonSimulator.setDisable(disabled);
        imgButtonSystem.setDisable(disabled);
    }

    /**
     * Method to verify that the text fields of the configurations.
     * @return true if there are no empty fields, it returns false.
     */
    private boolean validateFields(){
        if(!numberSimulationsText.getText().isEmpty() && !simulationTimeText.getText().isEmpty()
                && !kText.getText().isEmpty() && !nText.getText().isEmpty() && !pText.getText().isEmpty()
                && !tText.getText().isEmpty() && !mText.getText().isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Set the configuration parameters given by the user in an array
     * @return Array with system configuration
     */
    private Object[] parametersToArray(){
        Object parameters[] = new Object[8];
        parameters[NUMBER_SIMULATION] = Integer.parseInt(this.numberSimulationsText.getText());
        parameters[MAX_SIMULATION_TIME] = Double.parseDouble(this.simulationTimeText.getText());
        parameters[DELAY] = this.delayToggle.isSelected();
        parameters[K] = Integer.parseInt(this.kText.getText());
        parameters[N] = Integer.parseInt(this.nText.getText());
        parameters[P] = Integer.parseInt(this.pText.getText());
        parameters[M] = Integer.parseInt(this.mText.getText());
        parameters[T] = Double.parseDouble(this.kText.getText());
        return parameters;
    }

    /**
     * It shows the configuration of the servers of each module.
     * @param server Array with the number of servers of each module
     */
    private void showNumberServers(Object server[]){
        this.cServers.setText(server[K].toString());
        this.pServers.setText("1");
        this.qServers.setText(server[N].toString());
        this.tServers.setText(server[P].toString());
        this.eServers.setText(server[M].toString());
    }

    /**
     * Shows the size of the current queue of each module.
     * @param queueLength Array with the size of queue of each module
     */
    public void showQueueLength(int queueLength[]){
        this.cQueue.setText(String.valueOf(queueLength[M_CLIENTS]));
        this.pQueue.setText(String.valueOf(queueLength[M_PROCESSES]));
        this.qQueue.setText(String.valueOf(queueLength[M_QUERIES]));
        this.tQueue.setText(String.valueOf(queueLength[M_TRANSACTIONS]));
        this.eQueue.setText(String.valueOf(queueLength[M_EXECUTIONS]));
    }

    /**
     * Shows the number of DDL queries currently processed.
     * @param DDLQuantity Number of DDL queries processed by each module.
     */
    public void showDDLNumber(int DDLQuantity[]){
        this.cDDL.setText(String.valueOf(DDLQuantity[M_CLIENTS]));
        this.pDDL.setText(String.valueOf(DDLQuantity[M_PROCESSES]));
        this.qDDL.setText(String.valueOf(DDLQuantity[M_QUERIES]));
        this.tDDL.setText(String.valueOf(DDLQuantity[M_TRANSACTIONS]));
        this.eDDL.setText(String.valueOf(DDLQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of UPDATE queries currently processed.
     * @param UpdateQuantity Number of UPDATE queries processed by each module.
     */
    public void showUpdateNumber(int UpdateQuantity[]){
        this.cUpdate.setText(String.valueOf(UpdateQuantity[M_CLIENTS]));
        this.pUpdate.setText(String.valueOf(UpdateQuantity[M_PROCESSES]));
        this.qUpdate.setText(String.valueOf(UpdateQuantity[M_QUERIES]));
        this.tUpdate.setText(String.valueOf(UpdateQuantity[M_TRANSACTIONS]));
        this.eUpdate.setText(String.valueOf(UpdateQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of JOIN queries currently processed.
     * @param joinQuantity Number of JOIN queries processed by each module.
     */
    public void showJoinNumber(int joinQuantity[]){
        this.cJoin.setText(String.valueOf(joinQuantity[M_CLIENTS]));
        this.pJoin.setText(String.valueOf(joinQuantity[M_PROCESSES]));
        this.qJoin.setText(String.valueOf(joinQuantity[M_QUERIES]));
        this.tJoin.setText(String.valueOf(joinQuantity[M_TRANSACTIONS]));
        this.eJoin.setText(String.valueOf(joinQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of SELECT queries currently processed.
     * @param selectQuantity Number of SELECT queries processed by each module.
     */
    public void showSelectNumber(int selectQuantity[]){
        this.cSelect.setText(String.valueOf(selectQuantity[M_CLIENTS]));
        this.pSelect.setText(String.valueOf(selectQuantity[M_PROCESSES]));
        this.qSelect.setText(String.valueOf(selectQuantity[M_QUERIES]));
        this.tSelect.setText(String.valueOf(selectQuantity[M_TRANSACTIONS]));
        this.eSelect.setText(String.valueOf(selectQuantity[M_EXECUTIONS]));
    }

    /**
     * Update the graphical interface clock.
     * @param clock Simulator clock time
     */
    public void updateClock(double clock){
        this.clockTxt.setText(String.valueOf(String.format("%.2f",clock)));
    }

    /**
     * Update the number of connections discarded by the client module,
     * in the graphical interface.
     * @param discardedConnections currently discarded connections
     */
    public void updateDiscarded(int discardedConnections){
        this.discardedTxt.setText(String.valueOf(discardedConnections));
    }

    /**
     * Update the number of simulations executed.
     * @param simulationNumber number of simulations
     */
    public void updateSimulationNumber(int simulationNumber){
        this.simulationNumberTxt.setText(String.valueOf(simulationNumber));
    }

    /**
     * Update the number of connections that left the system by timeout.
     * @param timeoutNumber  Number of connections that made timeout
     */
    public void updateTimeoutNumber(int timeoutNumber){
        this.timeoutTxt.setText(String.valueOf(timeoutNumber));
    }
}
