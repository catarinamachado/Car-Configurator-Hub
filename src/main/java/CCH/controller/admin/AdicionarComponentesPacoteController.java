package CCH.controller.admin;

import CCH.CarConfiguratorHubApplication;
import CCH.business.CCH;
import CCH.business.Componente;
import CCH.business.Pacote;
import CCH.exception.ComponenteIncompativelNoPacoteException;
import CCH.exception.ComponenteJaExisteNoPacoteException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Optional;

public class AdicionarComponentesPacoteController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private CCH cch = CarConfiguratorHubApplication.getCch();

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

        addNewComponenteToPacoteButtonToTableColumn(observableList.get(3));

        table.setItems(getComponentes());
    }

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        componentes.addAll(cch.consultarComponentes());

        return componentes;
    }

    private void addNewComponenteToPacoteButtonToTableColumn(TableColumn t) {
        Callback<TableColumn<Componente, Void>, TableCell<Componente, Void>> cellFactory = new Callback<TableColumn<Componente, Void>, TableCell<Componente, Void>>() {
            @Override
            public TableCell<Componente, Void> call(final TableColumn<Componente, Void> param) {
                final TableCell<Componente, Void> cell = new TableCell<Componente, Void>() {

                    private final Button btn = new Button("Adicionar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Componente componente = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmação");
                            alert.setContentText("Pretende adicionar o componente ao pacote?");
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == ButtonType.OK) {
                                try {
                                    cch.adicionarComponenteAoPacote(pacote, componente.getId());
                                    table.refresh();
                                } catch (ComponenteJaExisteNoPacoteException e) {
                                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                    alert2.setTitle("Erro");
                                    alert2.setContentText("O componente já existe no pacote.");
                                    Optional<ButtonType> result2 = alert2.showAndWait();
                                } catch (ComponenteIncompativelNoPacoteException e) {
                                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                    alert2.setTitle("Erro");
                                    alert2.setHeaderText("Impossível Adicionar Componente");
                                    alert2.setContentText("É incompatível com o Componente " + e.getMessage() + ".");
                                    Optional<ButtonType> result2 = alert2.showAndWait();
                                }
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
    public void back() {
        back.getScene().getWindow().hide();
    }

}
