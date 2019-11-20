package trafficlight;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TrafficLightFXMLController implements Initializable {
    
    //FXML Variables
    @FXML private Circle redLight, yellowLight, greenLight;
    @FXML private Slider redSlider, yellowSlider, greenSlider;
    @FXML private Text redLabel, yellowLabel, greenLabel;
    @FXML private Button playButton, pauseButton;
    
    //Period of time yellow light should cycle (off 1/2 period, on other 1/2)
    private final double blinkCycle = 1;
    private final double lowOpacity = 0.5;
    
    //Other class variables
    private DoubleProperty redVal, yellowVal, greenVal;
    private BooleanProperty disableButton;
    private SequentialTransition sequence;
    
    /**
     * What the controller is to do on startup
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Turn off the lights
        this.darken(redLight);
        this.darken(yellowLight);
        this.darken(greenLight);
        
        //Bind the disabled properties of start and pause buttons
        disableButton = new SimpleBooleanProperty(false);
        playButton.disableProperty().bind(disableButton);
        pauseButton.disableProperty().bind(disableButton.not());
        
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
        
        //Finalize and start the program
        this.buildAnimation();
        this.startAnimation();
    }    
    
    /**
     * Start the animation when pushed
     */
    @FXML protected void startAnimation()
    {
        sequence.play();
        disableButton.set(true);
    }
    
    /**
     * Pause the animation where it is
     */
    @FXML protected void pauseAnimation()
    {
        sequence.pause();
        disableButton.set(false);
    }
    
    /**
     * Create the Sequential Transition and allows it to be rebuilt with changes
     */
    private void buildAnimation()
    {
        //Put together the yellow light flashing animation
        SequentialTransition yellowFlash = new SequentialTransition(
        new Timeline(
            new KeyFrame(Duration.seconds(0), e -> light(yellowLight)),
            new KeyFrame(Duration.seconds(blinkCycle/2), e -> darken(yellowLight)),
            new KeyFrame(Duration.seconds(blinkCycle))    
        ));
        
        //Finish up setup
        yellowFlash.setOnFinished(e -> darken(yellowLight));
        yellowFlash.setCycleCount((int)Math.round(yellowVal.divide(blinkCycle).get()));
        
        //Create hte rest of the sequence
        sequence = new SequentialTransition(
                new Timeline(
                    new KeyFrame(Duration.seconds(0), e -> light(redLight)),
                    new KeyFrame(Duration.seconds(redVal.get()), e -> darken(redLight))),
                new Timeline(
                    new KeyFrame(Duration.seconds(0), e -> light(greenLight)),
                    new KeyFrame(Duration.seconds(greenVal.get()), e -> darken(greenLight))),
                yellowFlash
        );
        
        //Set the sequence to rebuild and start after ending
        sequence.setOnFinished(e -> {buildAnimation(); sequence.play();});
        
        //Set cycle to 1
        sequence.setCycleCount(1);
    }
    
    /**
     * Brighten the shape by raising the opacity
     * @param target The shape to affect
     */
    private void light(Circle target)
    {
        target.setOpacity(1);
    }
    
    /**
     * Darken the shape by lowing the opacity
     * @param target The shape to affect
     */
    private void darken(Circle target)
    {
        target.setOpacity(lowOpacity);
    }
}
