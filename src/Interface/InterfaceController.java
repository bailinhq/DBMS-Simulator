package Interface;

import Controller.Application;
import Modules.Simulator;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.DoubleValidator;
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

import static Modules.Simulator.*;

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

    @FXML private JFXTextField kText;
    @FXML private JFXTextField nText;
    @FXML private JFXTextField pText;
    @FXML private JFXTextField mText;
    @FXML private JFXTextField tText;

    @FXML private JFXToggleButton delayToggle;


    @FXML private JFXTextField clockTxt;
    @FXML private JFXTextField discardedTxt;
    // Statistics
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


    public  InterfaceController(){}
    public InterfaceController(Application application){
        //this.application = application;
    }

    public void setApplication( Application application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideAll();
        welcomePanel.setVisible(true);
        welcomeArrow.setVisible(true);

        numberSimulationsText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        kText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        nText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        pText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        mText.addEventFilter(KeyEvent.ANY, handlerWholeNumbers);
        tText.addEventFilter(KeyEvent.ANY, handlerDecimalNumbers);

        simulationTimeText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    simulationTimeText.setText(oldValue);
                }
            }
        });

        tText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    tText.setText(oldValue);
                }
            }
        });



    }

    public void onExitButtonClicked(MouseEvent mouseEvent){
        Platform.exit();
        System.exit(0);
    }

    public void onWelcomeContinueButtonClicked(MouseEvent mouseEvent){
        hideAll();

        setDisabledSideButtons(false);

        simulatorArrow.setVisible(true);
        simulatorPanel.setVisible(true);
    }

    public void onSettingSimulatorContinueButtonClicked(MouseEvent mouseEvent){
        hideAll();

        systemPanel.setVisible(true);
        systemArrow.setVisible(true);

    }

    public void onSettingSystemRunButtonClicked(MouseEvent mouseEvent){
        if(validateFields()) {
            hideAll();
            setDisabledSideButtons(true);
            runArrow.setVisible(true);
            runPanel.setVisible(true);
            if ( application == null  )
            {
                application = new Application(this);
            }
            Object parameters[] = parametersToArray();
            showNumberServers(parameters);
            application.setUp(parameters);
            application.run2();
        }else{
            System.out.print("Faltan cosas");
        }
    }

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

    EventHandler<KeyEvent> handlerDecimalNumbers = new EventHandler<KeyEvent>() {
        private boolean isValid =false;
        @Override
        public void handle(KeyEvent event) {
            if(isValid)
                event.consume();
            if(!event.getCode().toString().matches("\\d(\\.)?\\d") && event.getCode() != KeyCode.BACK_SPACE
                    && !event.getCode().isDigitKey()&& event.getCode() != KeyCode.DECIMAL){
                if (event.getEventType() == KeyEvent.KEY_PRESSED)
                    isValid = true;
                else if( event.getEventType() == KeyEvent.KEY_RELEASED)
                    isValid = false;
            }
        }
    };

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

    private void setDisabledSideButtons(boolean disabled){
        imgbuttonSimulator.setDisable(disabled);
        imgButtonSystem.setDisable(disabled);
    }

    private boolean validateFields(){
        if(!numberSimulationsText.getText().isEmpty() && !simulationTimeText.getText().isEmpty()
                && !kText.getText().isEmpty() && !nText.getText().isEmpty() && !pText.getText().isEmpty()
                && !tText.getText().isEmpty() && !mText.getText().isEmpty()){
            return true;
        }
        return false;
    }

    public void prueba(){
        hideAll();
    }

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

    private void showNumberServers(Object server[]){
        this.cServers.setText(server[K].toString());
        this.pServers.setText("1");
        this.qServers.setText(server[N].toString());
        this.tServers.setText(server[P].toString());
        this.eServers.setText(server[M].toString());
    }

    public void showQueueLength(int queueLength[]){
        this.cQueue.setText(String.valueOf(queueLength[M_CLIENTS]));
        this.pQueue.setText(String.valueOf(queueLength[M_PROCESSES]));
        this.qQueue.setText(String.valueOf(queueLength[M_QUERIES]));
        this.tQueue.setText(String.valueOf(queueLength[M_TRANSACTIONS]));
        this.eQueue.setText(String.valueOf(queueLength[M_EXECUTIONS]));
    }

    public void showDDLNumber(int DDLQuantity[]){
        this.cDDL.setText(String.valueOf(DDLQuantity[M_CLIENTS]));
        this.pDDL.setText(String.valueOf(DDLQuantity[M_PROCESSES]));
        this.qDDL.setText(String.valueOf(DDLQuantity[M_QUERIES]));
        this.tDDL.setText(String.valueOf(DDLQuantity[M_TRANSACTIONS]));
        this.eDDL.setText(String.valueOf(DDLQuantity[M_EXECUTIONS]));
    }

    public void showUpdateNumber(int UpdateQuantity[]){
        this.cUpdate.setText(String.valueOf(UpdateQuantity[M_CLIENTS]));
        this.pUpdate.setText(String.valueOf(UpdateQuantity[M_PROCESSES]));
        this.qUpdate.setText(String.valueOf(UpdateQuantity[M_QUERIES]));
        this.tUpdate.setText(String.valueOf(UpdateQuantity[M_TRANSACTIONS]));
        this.eUpdate.setText(String.valueOf(UpdateQuantity[M_EXECUTIONS]));
    }

    public void showJoinNumber(int JoinQuantity[]){
        this.cJoin.setText(String.valueOf(JoinQuantity[M_CLIENTS]));
        this.pJoin.setText(String.valueOf(JoinQuantity[M_PROCESSES]));
        this.qJoin.setText(String.valueOf(JoinQuantity[M_QUERIES]));
        this.tJoin.setText(String.valueOf(JoinQuantity[M_TRANSACTIONS]));
        this.eJoin.setText(String.valueOf(JoinQuantity[M_EXECUTIONS]));
    }

    public void showSelectNumber(int SelectQuantity[]){
        this.cSelect.setText(String.valueOf(SelectQuantity[M_CLIENTS]));
        this.pSelect.setText(String.valueOf(SelectQuantity[M_PROCESSES]));
        this.qSelect.setText(String.valueOf(SelectQuantity[M_QUERIES]));
        this.tSelect.setText(String.valueOf(SelectQuantity[M_TRANSACTIONS]));
        this.eSelect.setText(String.valueOf(SelectQuantity[M_EXECUTIONS]));
    }

    public void updateClock(double clock){
        this.clockTxt.setText(String.valueOf(String.format("%.2f",clock)));
    }

    public void updateDiscarded(int discardedConnections){
        this.discardedTxt.setText(String.valueOf(discardedConnections));
    }
}
