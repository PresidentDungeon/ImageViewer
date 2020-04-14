package imageviewerproject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{
    private final List<Image> imagesSwitch = new ArrayList<>();
    private final List<String> titlesSwitch = new ArrayList<>();
    private int currentImageIndex = 0;
    private Scheduler imageScheduler = new Scheduler();

    @FXML
    Parent root;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnNext;

    @FXML
    private ImageView imageView;
    @FXML
    private Label textForPics;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    @FXML
    private void handleBtnLoadAction(ActionEvent event)
    {
         List<Image> images = new ArrayList<>();
         List<String> titles = new ArrayList<>();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images", 
            "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));        
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
                
        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
                imagesSwitch.add(new Image(f.toURI().toString()));
                titlesSwitch.add(f.getName());
                titles.add(f.getName());
            });
            Slideshow s = new Slideshow(imageView, textForPics, images, titles, currentImageIndex);
            imageScheduler.addSlideshow(s);
            displayImage();
            
        }
    }

    @FXML
    private void handleBtnPreviousAction(ActionEvent event)
    {
        if (!imagesSwitch.isEmpty())
        {
            currentImageIndex = 
                    (currentImageIndex - 1 + imagesSwitch.size()) % imagesSwitch.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction(ActionEvent event)
    {
        if (!imagesSwitch.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % imagesSwitch.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (!imagesSwitch.isEmpty())
        {
            imageView.setImage(imagesSwitch.get(currentImageIndex));
            textForPics.setText(titlesSwitch.get(currentImageIndex));
        }
    }

    @FXML
    private void handleStartSlideshow(ActionEvent event) {
        imageScheduler.startSlideshow();
    }

    @FXML
    private void handleStopSlideshow(ActionEvent event) throws InterruptedException, ExecutionException {
        imageScheduler.stopSlideshow();
    }



}
