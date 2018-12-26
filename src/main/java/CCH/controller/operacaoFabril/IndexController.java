package CCH.controller.operacaoFabril;

import CCH.CarConfiguratorHubApplication;
import CCH.business.Componente;
import CCH.business.Encomenda;
import CCH.business.OperacaoFabril;
import CCH.exception.SemEncomendasDisponiveisException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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

        ObservableList<Componente> componenteList = FXCollections.observableArrayList();
        componenteList.addAll(operacaoFabril.consultarComponentes());
        table.setItems(componenteList);
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
    }

    @FXML
    public void updateStock(CellEditEvent<Componente, String> event) {
            Componente componente = event.getTableView().getItems().get(event.getTablePosition().getRow());
            componente.setStock(Integer.parseInt(event.getNewValue()));

            try {
                Encomenda encomenda = operacaoFabril.atualizarStock(componente);
                idEncomenda.setText(Integer.toString(encomenda.getId()));
            } catch (SemEncomendasDisponiveisException e) {
                idEncomenda.setText("Nenhuma encomenda disponível");
            }

            table.refresh();
    }

    @FXML
    public void back() {
        back.getScene().getWindow().hide();
    }
}
