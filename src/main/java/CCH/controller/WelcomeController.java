package CCH.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class WelcomeController {
    @FXML
    public ImageView logo;

    @FXML
    public Button loginButton;

    @FXML
    public void initialize() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("logo.png").getFile());
        Image image = new Image(file.toURI().toString());
        logo.setImage(image);
    }

    @FXML
    public void onLoginButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));

        Stage stage = new Stage();
        stage.initOwner(logo.getScene().getWindow());
        stage.setScene(new Scene(loader.load()));

        stage.showAndWait();
    }
}
