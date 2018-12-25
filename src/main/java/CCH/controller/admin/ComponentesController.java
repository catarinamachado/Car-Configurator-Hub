package CCH.controller.admin;

import CCH.business.Pacote;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ComponentesController {
    @FXML
    public Button back;

    private Pacote pacote;

    public void setPacote(Pacote pacote) {
        this.pacote = pacote;
    }

    @FXML
    public void back() {
        back.getScene().getWindow().hide();
    }
}
