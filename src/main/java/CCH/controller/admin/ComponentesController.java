package CCH.controller.admin;

import CCH.business.Pacote;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ComponentesController {
    @FXML
    public Button back;

    private static Pacote pacote;

    public static void setPacote(Pacote newPacote) {
        pacote = newPacote;
    }

    @FXML
    public void back() {
        back.getScene().getWindow().hide();
    }
}
