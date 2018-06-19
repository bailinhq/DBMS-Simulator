package Interface;

import com.jfoenix.controls.JFXTextField;
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

public class InterfaceController implements Initializable {
    //Graphic interface elements
    //arrows
    @FXML
    private ImageView welcomeArrow;
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
    @FXML private JFXTextField timeSimulationsText;

    @FXML private JFXTextField kText;
    @FXML private JFXTextField nText;
    @FXML private JFXTextField pText;
    @FXML private JFXTextField mText;
    @FXML private JFXTextField tText;


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

        timeSimulationsText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    timeSimulationsText.setText(oldValue);
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
        if(!numberSimulationsText.getText().isEmpty() && !timeSimulationsText.getText().isEmpty()
                && !kText.getText().isEmpty() && !nText.getText().isEmpty() && !pText.getText().isEmpty()
                && !tText.getText().isEmpty() && !mText.getText().isEmpty()){
            return true;
        }
        return false;
    }


}
