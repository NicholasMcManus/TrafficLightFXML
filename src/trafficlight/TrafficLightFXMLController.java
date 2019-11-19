package trafficlight;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TrafficLightFXMLController implements Initializable {
    
    @FXML private Circle redLight, yellowLight, greenLight;
    @FXML private Slider redSlider, yellowSlider, greenSlider;
    @FXML private Text redLabel, yellowLabel, greenLabel;
    
    private DoubleProperty redVal, yellowVal, greenVal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Create the bindings
        redVal = new SimpleDoubleProperty();
        yellowVal = new SimpleDoubleProperty();
        greenVal = new SimpleDoubleProperty();
        
        //Bind the bindings
        redVal.bind(redSlider.valueProperty());        
        yellowVal.bind(yellowSlider.valueProperty());       
        greenVal.bind(greenSlider.valueProperty());
        
        //Set the text properties
        redLabel.textProperty().bind(Bindings.format("%3.0f", redVal).concat(" Seconds"));
        yellowLabel.textProperty().bind(Bindings.format("%3.0f", yellowVal).concat(" Seconds"));
        greenLabel.textProperty().bind(Bindings.format("%3.0f", greenVal).concat(" Seconds"));
    }    
    
}
