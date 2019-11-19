package trafficlight;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TrafficLightFXMLController implements Initializable {
    
    //FXML Variables
    @FXML private Circle redLight, yellowLight, greenLight;
    @FXML private Slider redSlider, yellowSlider, greenSlider;
    @FXML private Text redLabel, yellowLabel, greenLabel;
    
    //Period of time yellow light should cycle (off 1/2 period, on other 1/2)
    private final double blinkCycle = 1;
    private final double lowOpacity = 0.5;
    
    //Other class variables
    private DoubleProperty redVal, yellowVal, greenVal;
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
        
        this.buildAnimation();
        sequence.play();
    }    
    
    /**
     * Start the animation when pushed
     */
    @FXML protected void startAnimation()
    {
        sequence.play();
    }
    
    /**
     * Pause the animation where it is
     */
    @FXML protected void pauseAnimation()
    {
        sequence.pause();
    }
    
    /**
     * Create the Sequential Transition and allows it to be rebuilt with changes
     */
    private void buildAnimation()
    {
        System.out.println("Building Animation");
        
        SequentialTransition yellowFlash = new SequentialTransition(
        new Timeline(
            new KeyFrame(Duration.seconds(0), e -> light(yellowLight)),
            new KeyFrame(Duration.seconds(blinkCycle/2), e -> darken(yellowLight)),
            new KeyFrame(Duration.seconds(blinkCycle),e -> doNothing())
        ));
        
        yellowFlash.setOnFinished(e -> darken(yellowLight));
        yellowFlash.setCycleCount((int)Math.round(yellowVal.divide(blinkCycle).get()));
        
        sequence = new SequentialTransition(
                new Timeline(
                    new KeyFrame(Duration.seconds(0), e -> light(redLight)),
                    new KeyFrame(Duration.seconds(redVal.get()), e -> darken(redLight))),
                new Timeline(
                    new KeyFrame(Duration.seconds(0), e -> light(greenLight)),
                    new KeyFrame(Duration.seconds(greenVal.get()), e -> darken(greenLight))),
                yellowFlash
        );
        
        //Set indefinite
        sequence.setOnFinished(e -> {buildAnimation(); sequence.play();});
        
        //Set 
        sequence.setCycleCount(1);
    }
    
    /**
     * Does nothing to create the end of a Timeline
     */
    private void doNothing()
    {
        //Seriously, This is only called to make space in a timeline
    }
    
    /**
     * Brighten the shape by raising the opacity
     * @param target The shape to affect
     */
    private void light(Circle target)
    {
        target.setOpacity(1);
        //System.out.println("Light, " + target);
    }
    
    /**
     * Darken the shape by lowing the opacity
     * @param target The shape to affect
     */
    private void darken(Circle target)
    {
        target.setOpacity(lowOpacity);
        //System.out.println("Turn off, " + target);
    }
}
