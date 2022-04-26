package tierklinik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/TableList.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Tierklinik System");

            Image icon = new Image("pet.png");
            stage.getIcons().add(icon);
            stage.setTitle("Tierklinik System");
            stage.setWidth(1920);
            stage.setHeight(1080);

            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}