package CCH.controller.gestaoDeConfiguracao;

import CCH.CarConfiguratorHubApplication;
import CCH.business.Componente;
import CCH.business.Configuracao;
import CCH.business.GestaoDeConfiguracao;
import CCH.business.Pacote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ConfiguracaoController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    @FXML
    public TableView tablepacs;

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
        table.refresh();

        ObservableList<TableColumn> observableListPacs = tablepacs.getColumns();

        observableListPacs.get(0).setCellValueFactory(
                new PropertyValueFactory<Pacote, String>("nome")
        );

        addComponenteButtonToTableColumn(observableListPacs.get(1));

        observableListPacs.get(2).setCellValueFactory(
                new PropertyValueFactory<Pacote, Integer>("desconto")
        );

        addDeleteButtonToTableColumnPacs(observableListPacs.get(3));

        tablepacs.setItems(getPacotes());
        tablepacs.refresh();

    }

    private ObservableList<Pacote> getPacotes() {
        ObservableList<Pacote> pacotes = FXCollections.observableArrayList();
        pacotes.addAll(configuracao.consultarPacotes().values());
        return pacotes;
    }

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        componentes.addAll(configuracao.componentesNotInPacotes().values());
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
                            if (result.get() == ButtonType.OK) {
                                List<Componente> requeridos = configuracao.componentesRequeremMeNaConfig(componente.getId());
                                boolean flag = true;

                                if (requeridos.size() != 0)
                                    flag = temRequeridos(requeridos);

                                if (flag)
                                    configuracao.removerComponente(componente.getId());

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
    public void loadComponentes() throws IOException {
        ComponentesController.setConfiguracao(configuracao);
        redirectTo("/views/gestaoDeConfiguracao/componentes.fxml");
    }

    @FXML
    public void loadPacotes() throws IOException {
        PacotesController.setConfiguracao(configuracao);
        redirectTo("/views/gestaoDeConfiguracao/pacotes.fxml");
    }

    @FXML
    public void configOtima() throws IOException {
        ConfiguracaoOtimaController.setConfiguracao(configuracao);
        redirectTo("/views/gestaoDeConfiguracao/configuracaootima.fxml");
        back();
    }

    @FXML
    public void criarEncomenda() throws IOException {
        EncomendaController.setConfiguracao(configuracao);
        redirectTo("/views/gestaoDeConfiguracao/encomenda.fxml");
    }

    private void redirectTo(String fxml) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(back.getScene().getWindow());

        stage.setScene(
                new Scene(
                        new FXMLLoader(getClass().getResource(fxml)).load()));

        stage.showAndWait();
        initialize();
    }

    private void addComponenteButtonToTableColumn(TableColumn t) {
        Callback<TableColumn<Pacote, Void>, TableCell<Pacote, Void>> cellFactory = new Callback<TableColumn<Pacote, Void>, TableCell<Pacote, Void>>() {
            @Override
            public TableCell<Pacote, Void> call(final TableColumn<Pacote, Void> param) {
                final TableCell<Pacote, Void> cell = new TableCell<Pacote, Void>() {

                    private final Button btn = new Button("Abrir");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Pacote pacote = getTableView().getItems().get(getIndex());
                            loadComponentes(pacote);
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

    private void loadComponentes(Pacote pacote) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/gestaoDeConfiguracao/componentesNoPacote.fxml"));
            ComponentesNoPacoteController.setPacote(pacote);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initOwner(back.getScene().getWindow());
            stage.setScene(scene);

            stage.showAndWait();
            initialize();
        } catch (IOException e) { }
    }

    private void addDeleteButtonToTableColumnPacs(TableColumn t) {
        Callback<TableColumn<Pacote, Void>, TableCell<Pacote, Void>> cellFactory = new Callback<TableColumn<Pacote, Void>, TableCell<Pacote, Void>>() {
            @Override
            public TableCell<Pacote, Void> call(final TableColumn<Pacote, Void> param) {
                final TableCell<Pacote, Void> cell = new TableCell<Pacote, Void>() {

                    private final Button btn = new Button("Apagar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Pacote pacote = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Aviso");
                            alert.setHeaderText("Os componentes do " + pacote.getNome() + " não serão removidos.");
                            alert.setContentText("Pretende continuar ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                configuracao.removerPacote(pacote.getId());

                                table.setItems(getComponentes());
                                table.refresh();
                                tablepacs.setItems(getPacotes());
                                tablepacs.refresh();
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

    public boolean temRequeridos(List<Componente> componentes) {
        StringBuilder str = new StringBuilder("Componente: ");

        int i = 0;
        for(i = 0; i < componentes.size() - 1 ; i++) {
            str.append(componentes.get(i).getFullName() + ", ");
        }
        str.append(componentes.get(i).getFullName() + ".");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
            alert.setHeaderText("O componente que pretende remover é requerido pelo " +
                    str);

        alert.setContentText("Pretende remover o componente na mesma?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
