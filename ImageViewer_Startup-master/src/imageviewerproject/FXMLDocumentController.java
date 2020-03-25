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
import java.util.concurrent.Future;
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
    private final List<Image> images = new CopyOnWriteArrayList<>();
    private final List<String> titles = new CopyOnWriteArrayList<>();
    private int currentImageIndex = 0;
    private ExecutorService executor;
    private Future<Integer> index;

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
                titles.add(f.getName());
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction(ActionEvent event)
    {
        if (!images.isEmpty())
        {
            currentImageIndex = 
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction(ActionEvent event)
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    @FXML
    private void handleStartSlideshow(ActionEvent event) {
        Slideshow s = new Slideshow(imageView, textForPics, images, titles, currentImageIndex);
        executor = Executors.newSingleThreadExecutor();
        index = executor.submit(s);

    }

    @FXML
    private void handleStopSlideshow(ActionEvent event) throws InterruptedException, ExecutionException {
        executor.shutdownNow();
        currentImageIndex = index.get();
    }



}
