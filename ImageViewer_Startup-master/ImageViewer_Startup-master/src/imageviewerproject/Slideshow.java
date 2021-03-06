package imageviewerproject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Slideshow implements Runnable {

    private final long DELAY = 1;
    private int index = 0;
    private ImageView imageView;
    private Label lblFilename;
    private List<Image> images;
    private List<String> filenames;
    private ExecutorService executor;

    public Slideshow(ImageView imageView, Label label,
            List<Image> images, List<String> filenames, int index) {
        this.imageView = imageView;
        this.images = images;
        this.lblFilename = label;
        this.filenames = filenames;
        this.index = index;
    }

    @Override
    public void run() {
        if (!images.isEmpty()) {
            try {
                while (true) {

                    Platform.runLater(() -> {
                        lblFilename.setText(filenames.get(index));
                        imageView.setImage(images.get(index));
                    });

                    index = (index + 1) % images.size();
                    TimeUnit.SECONDS.sleep(DELAY);
                }
            } catch (InterruptedException ex) {
            }

        }
    }

    public void handleStartSlideshow() {
        if (executor != null && !executor.isShutdown())
        {
            executor.shutdownNow();
        }
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }

    public void handleStopSlideshow() {
        executor.shutdownNow();
    }

}
