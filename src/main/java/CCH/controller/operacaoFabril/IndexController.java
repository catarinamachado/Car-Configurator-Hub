package CCH.controller.operacaoFabril;

import CCH.CarConfiguratorHubApplication;
import CCH.business.Componente;
import CCH.business.Encomenda;
import CCH.business.OperacaoFabril;
import CCH.exception.SemEncomendasDisponiveisException;
import CCH.exception.StockInvalidoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class IndexController {
    @FXML
    public TableView table;

    @FXML
    public Label idEncomenda;

    @FXML
    public Button back;

    private OperacaoFabril operacaoFabril = CarConfiguratorHubApplication.getCch().getOperacaoFabril();

    @FXML
    public void initialize() {
        try {
            Encomenda encomenda = operacaoFabril.consultarProximaEncomenda();
            idEncomenda.setText(Integer.toString(encomenda.getId()));
        } catch (SemEncomendasDisponiveisException e) {
            idEncomenda.setText("Nenhuma encomenda disponível");
        }

        table.setEditable(true);
        table.getSelectionModel().cellSelectionEnabledProperty().set(true);

        ObservableList<TableColumn> observableList = table.getColumns();

        observableList.get(0).setCellValueFactory(
                new PropertyValueFactory<Componente, String>("fullName")
        );

        TableColumn<Componente, String> stockColumn = observableList.get(1);
        stockColumn.setCellValueFactory(
                new PropertyValueFactory<>("stockString")
        );
        stockColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        stockColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<Componente, String>>() {
                    @Override
                    public void handle(CellEditEvent<Componente, String> t) {
                        updateStock(t);
                    }
                }
        );

        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory<Componente, Double>("preco")
        );

        table.setItems(getComponentes());
}

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentesList = FXCollections.observableArrayList();
        componentesList.addAll(operacaoFabril.consultarComponentes());

        return componentesList;
    }

    @FXML
    public void concluir() {
        try {
            int encomendaId = Integer.parseInt(idEncomenda.getText());
            operacaoFabril.removerEncomenda(encomendaId);
        } catch (Exception e) {}

        try {
            Encomenda encomenda = operacaoFabril.consultarProximaEncomenda();
            idEncomenda.setText(Integer.toString(encomenda.getId()));
        } catch (SemEncomendasDisponiveisException e) {
            idEncomenda.setText("Nenhuma encomenda disponível");
        }

        table.setItems(getComponentes());
    }

    @FXML
    public void updateStock(CellEditEvent<Componente, String> event) {
            Componente componente = event.getTableView().getItems().get(event.getTablePosition().getRow());
            componente.setStock(Integer.parseInt(event.getNewValue()));

            try {
                Encomenda encomenda = operacaoFabril.atualizarStock(componente);
                idEncomenda.setText(Integer.toString(encomenda.getId()));
            } catch (StockInvalidoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Stock Inválido");
                alert.setContentText("O stock inserido não pode ser menor do que 0.");

                alert.showAndWait();
            } catch (SemEncomendasDisponiveisException e) {
                idEncomenda.setText("Nenhuma encomenda disponível");
            }

            table.refresh();
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
