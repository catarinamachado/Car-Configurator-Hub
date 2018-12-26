package CCH.controller.gestaoDeConfiguracao;

import CCH.CarConfiguratorHubApplication;
import CCH.business.CCH;
import CCH.business.Componente;
import CCH.business.Configuracao;
import CCH.business.GestaoDeConfiguracao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Optional;

public class ConfiguracaoController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private static Configuracao configuracao;
    public static void setConfiguracao(Configuracao newConfiguracao) {
        configuracao = newConfiguracao;
    }

    private GestaoDeConfiguracao gestaoDeConfiguracao = CarConfiguratorHubApplication.getCch().getGestaoDeConfiguracao();

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

        addDeleteButtonToTableColumn(observableList.get(3));

        table.setItems(getComponentes());
    }

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        componentes.addAll(
                gestaoDeConfiguracao.getComponentes(
                                configuracao.getId()));
        return componentes;
    }

    private void addDeleteButtonToTableColumn(TableColumn t) {
        Callback<TableColumn<Componente, Void>, TableCell<Componente, Void>> cellFactory = new Callback<TableColumn<Componente, Void>, TableCell<Componente, Void>>() {
            @Override
            public TableCell<Componente, Void> call(final TableColumn<Componente, Void> param) {
                final TableCell<Componente, Void> cell = new TableCell<Componente, Void>() {

                    private final Button btn = new Button("Apagar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Componente componente = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Aviso");
                            alert.setHeaderText("O " + componente.getFullName() + " será removido.");
                            alert.setContentText("Pretende continuar?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                gestaoDeConfiguracao.removerComponente(configuracao.getId(), componente.getId());
                                table.setItems(getComponentes());
                                table.refresh();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        t.setCellFactory(cellFactory);
    }

    @FXML
    public void loadComponentes() {

    }

    @FXML
    public void loadPacotes() {

    }

    @FXML
    public void configOtima() {

    }

    @FXML
    public void criarEncomenda() {

    }



    @FXML
    public void back() {
        back.getScene().getWindow().hide();
    }
}
