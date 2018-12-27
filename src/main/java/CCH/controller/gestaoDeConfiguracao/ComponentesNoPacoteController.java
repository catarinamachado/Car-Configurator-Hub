package CCH.controller.gestaoDeConfiguracao;

import CCH.business.Componente;
import CCH.business.Pacote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ComponentesNoPacoteController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private static Pacote pacote;
    public static void setPacote(Pacote newPacote) {
        pacote = newPacote;
    }

    @FXML
    public void initialize() {
        ObservableList<TableColumn> observableList = table.getColumns();

        observableList.get(0).setCellValueFactory(
                new PropertyValueFactory<Componente, String>("fullName")
        );

        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory<Componente, Double>("stockAvailable")
        );

        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory<Componente, Double>("preco")
        );

        table.setItems(getComponentes());
    }

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        componentes.addAll(pacote.getComponentes().values());

        return componentes;
    }


    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
