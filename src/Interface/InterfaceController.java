package Interface;

import Controller.Application;
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

public class InterfaceController implements Initializable{
    Application application;

    //constant values
    static final int NUMBER_SIMULATION = 0;
    static final int MAX_SIMULATION_TIME = 1;
    static final int DELAY = 2;
    static final int K = 3;
    static final int N = 4;
    static final int P = 5;
    static final int M = 6;
    static final int T = 7;

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
            application.setUp(parametersToArray());
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
}
