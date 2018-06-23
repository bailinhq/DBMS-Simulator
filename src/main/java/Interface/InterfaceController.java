package main.java.Interface;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Label;
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
import java.util.concurrent.Semaphore;


import static main.java.Simulator.*;

public class InterfaceController implements Initializable{
    Application application;
    public Semaphore semaphore;

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
    @FXML private AnchorPane runPanelAverage;
    @FXML private AnchorPane statsPanel;

    //Text Field
    @FXML private JFXTextField numberSimulationsText;
    @FXML private JFXTextField simulationTimeText;
    @FXML private JFXTextField timeoutTxt;
    @FXML private JFXTextField timeoutTxtAverage;

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

    //Update
    @FXML private JFXTextField cUpdate;
    @FXML private JFXTextField pUpdate;
    @FXML private JFXTextField qUpdate;
    @FXML private JFXTextField tUpdate;
    @FXML private JFXTextField eUpdate;

    //Join
    @FXML private JFXTextField cJoin;
    @FXML private JFXTextField pJoin;
    @FXML private JFXTextField qJoin;
    @FXML private JFXTextField tJoin;
    @FXML private JFXTextField eJoin;

    //Select
    @FXML private JFXTextField cSelect;
    @FXML private JFXTextField pSelect;
    @FXML private JFXTextField qSelect;
    @FXML private JFXTextField tSelect;
    @FXML private JFXTextField eSelect;


    //-----------------------------------------------------------//
    //                      Statistics by run
    //----------------------------------------------------------//
    @FXML private JFXTextField clockTxtAverage;
    @FXML private JFXTextField discardedTxtAverage;
    @FXML private JFXTextField simulationNumberTxtAverage;

    //queue length
    @FXML private JFXTextField cQueueAverage;
    @FXML private JFXTextField pQueueAverage;
    @FXML private JFXTextField qQueueAverage;
    @FXML private JFXTextField tQueueAverage;
    @FXML private JFXTextField eQueueAverage;

    //DDL
    @FXML private JFXTextField cDDLAverage;
    @FXML private JFXTextField pDDLAverage;
    @FXML private JFXTextField qDDLAverage;
    @FXML private JFXTextField tDDLAverage;
    @FXML private JFXTextField eDDLAverage;

    //Update
    @FXML private JFXTextField cUpdateAverage;
    @FXML private JFXTextField pUpdateAverage;
    @FXML private JFXTextField qUpdateAverage;
    @FXML private JFXTextField tUpdateAverage;
    @FXML private JFXTextField eUpdateAverage;

    //Join
    @FXML private JFXTextField cJoinAverage;
    @FXML private JFXTextField pJoinAverage;
    @FXML private JFXTextField qJoinAverage;
    @FXML private JFXTextField tJoinAverage;
    @FXML private JFXTextField eJoinAverage;

    //Select
    @FXML private JFXTextField cSelectAverage;
    @FXML private JFXTextField pSelectAverage;
    @FXML private JFXTextField qSelectAverage;
    @FXML private JFXTextField tSelectAverage;
    @FXML private JFXTextField eSelectAverage;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        semaphore = new Semaphore(0);

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
     * Method to show the panel of system configurations, by pressing the
     * continue button in the simulator settings panel.
     * @param mouseEvent Mouse click event
     */
    public void onNextButtonClicked(MouseEvent mouseEvent){
        this.semaphore.release();
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
            application.start();
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

        runPanelAverage.setVisible(false);
    }

    /**
     * Method to show statistics by execution.
     * @param mouseEvent Mouse click event
     */
    public void onShowStatsButtonClicked(MouseEvent mouseEvent){
        hideAll();
        runPanelAverage.setVisible(true);
        runArrow.setVisible(true);
    }

    /**
     * Method to show run panel.
     * @param mouseEvent Mouse click event
     */
    public void onShowRealtimeButtonClicked(MouseEvent mouseEvent){
        hideAll();
        runPanel.setVisible(true);
        runArrow.setVisible(true);
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
        updateUI(this.cServers,server[K].toString());
        updateUI(this.pServers,"1");
        updateUI(this.qServers,server[N].toString());
        updateUI(this.tServers,server[P].toString());
        updateUI(this.eServers,server[M].toString());
    }

    /**
     * Shows the size of the current queue of each module.
     * @param queueLength Array with the size of queue of each module
     */
    public void showQueueLength(int queueLength[]){
        updateUI(this.cQueue,String.valueOf(queueLength[M_CLIENTS]));
        updateUI(this.pQueue,String.valueOf(queueLength[M_PROCESSES]));
        updateUI(this.qQueue,String.valueOf(queueLength[M_QUERIES]));
        updateUI(this.tQueue,String.valueOf(queueLength[M_TRANSACTIONS]));
        updateUI(this.eQueue,String.valueOf(queueLength[M_EXECUTIONS]));
    }

    /**
     * Shows the number of DDL queries currently processed.
     * @param DDLQuantity Number of DDL queries processed by each module.
     */
    public void showDDLNumber(int DDLQuantity[]){
        updateUI(this.cDDL,String.valueOf(DDLQuantity[M_CLIENTS]));
        updateUI(this.pDDL,String.valueOf(DDLQuantity[M_PROCESSES]));
        updateUI(this.qDDL,String.valueOf(DDLQuantity[M_QUERIES]));
        updateUI(this.tDDL,String.valueOf(DDLQuantity[M_TRANSACTIONS]));
        updateUI(this.eDDL,String.valueOf(DDLQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of UPDATE queries currently processed.
     * @param UpdateQuantity Number of UPDATE queries processed by each module.
     */
    public void showUpdateNumber(int UpdateQuantity[]){
        updateUI(this.cUpdate,String.valueOf(UpdateQuantity[M_CLIENTS]));
        updateUI(this.pUpdate,String.valueOf(UpdateQuantity[M_PROCESSES]));
        updateUI(this.qUpdate,String.valueOf(UpdateQuantity[M_QUERIES]));
        updateUI(this.tUpdate,String.valueOf(UpdateQuantity[M_TRANSACTIONS]));
        updateUI(this.eUpdate,String.valueOf(UpdateQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of JOIN queries currently processed.
     * @param joinQuantity Number of JOIN queries processed by each module.
     */
    public void showJoinNumber(int joinQuantity[]){
        updateUI(this.cJoin,String.valueOf(joinQuantity[M_CLIENTS]));
        updateUI(this.pJoin,String.valueOf(joinQuantity[M_PROCESSES]));
        updateUI(this.qJoin,String.valueOf(joinQuantity[M_QUERIES]));
        updateUI(this.tJoin,String.valueOf(joinQuantity[M_TRANSACTIONS]));
        updateUI(this.eJoin,String.valueOf(joinQuantity[M_EXECUTIONS]));
    }

    /**
     * Shows the number of SELECT queries currently processed.
     * @param selectQuantity Number of SELECT queries processed by each module.
     */
    public void showSelectNumber(int selectQuantity[]){
        updateUI(this.cSelect,String.valueOf(selectQuantity[M_CLIENTS]));
        updateUI(this.pSelect,String.valueOf(selectQuantity[M_PROCESSES]));
        updateUI(this.qSelect,String.valueOf(selectQuantity[M_QUERIES]));
        updateUI(this.tSelect,String.valueOf(selectQuantity[M_TRANSACTIONS]));
        updateUI(this.eSelect,String.valueOf(selectQuantity[M_EXECUTIONS]));
    }




    /**
     * Update the graphical interface clock.
     * @param clock Simulator clock time
     */
    public void updateClock(double clock){
        updateUI(this.clockTxt,String.valueOf(String.format("%.2f",clock)));
    }

    /**
     * Update the number of connections discarded by the client module,
     * in the graphical interface.
     * @param discardedConnections currently discarded connections
     */
    public void updateDiscarded(int discardedConnections){
        updateUI(discardedTxt,String.valueOf(discardedConnections));
        updateUI(discardedTxtAverage,String.valueOf(discardedConnections));
    }

    private void updateUI(JFXTextField jfxTextField, String data){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {

                Platform.runLater(() -> jfxTextField.setText(data));
                return null;

            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Update the number of simulations executed.
     * @param simulationNumber number of simulations
     */
    public synchronized void updateSimulationNumber(int simulationNumber){
        updateUI(simulationNumberTxt, String.valueOf(simulationNumber));
        updateUI(simulationNumberTxtAverage, String.valueOf(simulationNumber));
    }

    /**
     * Update the number of connections that left the system by timeout.
     * @param timeoutNumber  Number of connections that made timeout
     */
    public void updateTimeoutNumber(int timeoutNumber){
        updateUI(this.timeoutTxt,String.valueOf(timeoutNumber));
        updateUI(this.timeoutTxtAverage,String.valueOf(timeoutNumber));
    }


    //---------------------------------------
    //             run average panel
    //---------------------------------------
    /**
     * Shows the average size of the current queue of each module.
     * @param averageQueueLength Array with the size of queue of each module
     */
    public void showQueueAverageLength(double averageQueueLength[]){ 
        updateUI(this.cQueueAverage,String.valueOf(String.format("%.2f",averageQueueLength[M_CLIENTS])));
        updateUI(this.pQueueAverage,String.valueOf(String.format("%.2f",averageQueueLength[M_PROCESSES])));
        updateUI(this.qQueueAverage,String.valueOf(String.format("%.2f",averageQueueLength[M_QUERIES])));
        updateUI(this.tQueueAverage,String.valueOf(String.format("%.2f",averageQueueLength[M_TRANSACTIONS])));
        updateUI(this.eQueueAverage,String.valueOf(String.format("%.2f",averageQueueLength[M_EXECUTIONS])));
    }

    /**
     * Shows the average number of DDL queries currently processed.
     * @param DDLAverageTime Number of DDL queries processed by each module.
     */
    public void showDDLAverageTime(double DDLAverageTime[]){ 
        updateUI(this.cDDLAverage,String.valueOf(String.format("%.2f",DDLAverageTime[M_CLIENTS])));
        updateUI(this.pDDLAverage,String.valueOf(String.format("%.2f",DDLAverageTime[M_PROCESSES])));
        updateUI(this.qDDLAverage,String.valueOf(String.format("%.2f",DDLAverageTime[M_QUERIES])));
        updateUI(this.tDDLAverage,String.valueOf(String.format("%.2f",DDLAverageTime[M_TRANSACTIONS])));
        updateUI(this.eDDLAverage,String.valueOf(String.format("%.2f",DDLAverageTime[M_EXECUTIONS])));
    }

    /**
     * Shows the average number of UPDATE queries currently processed.
     * @param UpdateQuantity Number of UPDATE queries processed by each module.
     */
    public void showUpdateAverageTime(double UpdateQuantity[]){
        updateUI(this.cUpdateAverage,String.valueOf(String.format("%.2f",UpdateQuantity[M_CLIENTS])));
        updateUI(this.pUpdateAverage,String.valueOf(String.format("%.2f",UpdateQuantity[M_PROCESSES])));
        updateUI(this.qUpdateAverage,String.valueOf(String.format("%.2f",UpdateQuantity[M_QUERIES])));
        updateUI(this.tUpdateAverage,String.valueOf(String.format("%.2f",UpdateQuantity[M_TRANSACTIONS])));
        updateUI(this.eUpdateAverage,String.valueOf(String.format("%.2f",UpdateQuantity[M_EXECUTIONS])));
    }

    /**
     * Shows the average number of JOIN queries currently processed.
     * @param joinQuantity Number of JOIN queries processed by each module.
     */
    public void showJoinAverageTime(double joinQuantity[]){
        updateUI(this.cJoinAverage,String.valueOf(String.format("%.2f",joinQuantity[M_CLIENTS])));
        updateUI(this.pJoinAverage,String.valueOf(String.format("%.2f",joinQuantity[M_PROCESSES])));
        updateUI(this.qJoinAverage,String.valueOf(String.format("%.2f",joinQuantity[M_QUERIES])));
        updateUI(this.tJoinAverage,String.valueOf(String.format("%.2f",joinQuantity[M_TRANSACTIONS])));
        updateUI(this.eJoinAverage,String.valueOf(String.format("%.2f",joinQuantity[M_EXECUTIONS])));
    }

    /**
     * Shows the average number of SELECT queries currently processed.
     * @param selectQuantity Number of SELECT queries processed by each module.
     */
    public void showSelectAverageTime(double selectQuantity[]){
        updateUI(this.cSelectAverage,String.valueOf(String.format("%.2f",selectQuantity[M_CLIENTS])));
        updateUI(this.pSelectAverage,String.valueOf(String.format("%.2f",selectQuantity[M_PROCESSES])));
        updateUI(this.qSelectAverage,String.valueOf(String.format("%.2f",selectQuantity[M_QUERIES])));
        updateUI(this.tSelectAverage,String.valueOf(String.format("%.2f",selectQuantity[M_TRANSACTIONS])));
        updateUI(this.eSelectAverage,String.valueOf(String.format("%.2f",selectQuantity[M_EXECUTIONS])));
    }

    /**
     * Update the average lifetime of a query
     * @param lifeTime lifetime of queries
     */
    public void showAverageLifetimeQuery(double lifeTime){
        updateUI(this.clockTxtAverage,String.valueOf(String.format("%.2f",lifeTime)));
    }



}
