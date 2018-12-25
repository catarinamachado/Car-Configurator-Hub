package CCH.controller;

import CCH.CarConfiguratorHubApplication;
import CCH.business.CCH;
import CCH.business.TipoUtilizador;
import CCH.business.Utilizador;
import CCH.exception.WrongCredentialsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {
    @FXML
    public Button loginButton;

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    private CCH cch = CarConfiguratorHubApplication.getCch();

    public void login() {
        try {
            int id = Integer.parseInt(usernameField.getText());
            String password = passwordField.getText();

            Utilizador u = cch.getUtilizadorDAO().get(id);

            if (u.validarCredenciais(id, password)) {
                TipoUtilizador tipoUtilizador = u.getTipoUtilizador();

                if (tipoUtilizador == TipoUtilizador.ADMIN) {
                    redirectTo("/views/admin/pacotes.fxml");
                } else if (tipoUtilizador == TipoUtilizador.FABRICA) {
                    redirectTo("/views/operacaoFabril/index.fxml");
                } else {
                    redirectTo("/views/gestaoDeConfiguracao/index.fxml");
                }

            } else {
                throw new WrongCredentialsException();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Invalid credentials");
            alert.showAndWait();
        }
    }

    private void redirectTo(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
    }
}
