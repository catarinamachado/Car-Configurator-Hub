package CCH.controller.admin;

import CCH.CarConfiguratorHubApplication;
import CCH.business.*;
import CCH.exception.TipoUtilizadorInexistenteException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Optional;


public class FuncionariosController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private CCH cch = CarConfiguratorHubApplication.getCch();

    @FXML
    public void initialize() {
        table.setEditable(true);
        table.getSelectionModel().cellSelectionEnabledProperty().set(true);

        ObservableList<TableColumn> observableList = table.getColumns();

        TableColumn<Utilizador, String> userColumn = observableList.get(0);
        userColumn.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );
        userColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        userColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Utilizador, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Utilizador, String> t) {
                        updateUser(t);
                    }
                }
        );

        TableColumn<Utilizador, String> passwordColumn = observableList.get(1);
        passwordColumn.setCellValueFactory(
                new PropertyValueFactory<>("password")
        );
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Utilizador, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Utilizador, String> t) {
                        updatePassword(t);
                    }
                }
        );

        observableList.get(2).setCellValueFactory(
                new PropertyValueFactory<Utilizador, String>("nomeUtilizador")
        );

        TableColumn<Utilizador, String> tipoUtilizadorColumn = observableList.get(3);
        tipoUtilizadorColumn.setCellValueFactory(
                new PropertyValueFactory<>("nomeTipoUtilizador")
        );
        tipoUtilizadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tipoUtilizadorColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Utilizador, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Utilizador, String> t) {
                        updateTipo(t);
                    }
                }
        );

        addDeleteButtonToTableColumn(observableList.get(4));

        table.setItems(getUtilizadores());
    }

    private ObservableList<Utilizador> getUtilizadores() {
        ObservableList<Utilizador> utilizadores = FXCollections.observableArrayList();
        utilizadores.addAll(cch.consultarFuncionarios());

        return utilizadores;
    }

    private void addDeleteButtonToTableColumn(TableColumn t) {
        Callback<TableColumn<Utilizador, Void>, TableCell<Utilizador, Void>> cellFactory = new Callback<TableColumn<Utilizador, Void>, TableCell<Utilizador, Void>>() {
            @Override
            public TableCell<Utilizador, Void> call(final TableColumn<Utilizador, Void> param) {
                final TableCell<Utilizador, Void> cell = new TableCell<Utilizador, Void>() {

                    private final Button btn = new Button("Apagar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Utilizador utilizador = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Aviso");
                            alert.setContentText("Pretende continuar?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                cch.removerUtilizador(utilizador.getId());
                                table.setItems(getUtilizadores());
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
    public void updateUser(TableColumn.CellEditEvent<Utilizador, String> event) {
        Utilizador utilizador = event.getTableView().getItems().get(event.getTablePosition().getRow());
        utilizador.setNome(event.getNewValue());
        utilizador.atualizarUser(utilizador);

        table.refresh();
    }

    @FXML
    public void updatePassword(TableColumn.CellEditEvent<Utilizador, String> event) {
        Utilizador utilizador = event.getTableView().getItems().get(event.getTablePosition().getRow());
        utilizador.setPassword(event.getNewValue());
        utilizador.atualizarPassword(utilizador);

        table.refresh();
    }

    @FXML
    public void updateTipo(TableColumn.CellEditEvent<Utilizador, String> event) {
        Utilizador utilizador = event.getTableView().getItems().get(event.getTablePosition().getRow());

        try {
            int value = utilizador.parseNomeTipoToValue(event.getNewValue());
            utilizador.setTipoUtilizadorValue(value);
            utilizador.atualizarTipo(utilizador);
        }
        catch (TipoUtilizadorInexistenteException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tipo de Funcionário Incorreto");
            alert.setHeaderText("Tipo de Funcionário não existe");
            alert.setContentText("O tipo de funcionário não foi alterado.");

            alert.showAndWait();
        }

        table.refresh();
    }

    @FXML
    public void add() {
        cch.criarUtilizador();
        table.setItems(getUtilizadores());
        table.refresh();
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }

}
