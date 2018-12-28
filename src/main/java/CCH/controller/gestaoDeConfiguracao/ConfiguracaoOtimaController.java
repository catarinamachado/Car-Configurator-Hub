package CCH.controller.gestaoDeConfiguracao;

import CCH.business.*;
import CCH.exception.NoOptimalConfigurationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import CCH.CarConfiguratorHubApplication;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ConfiguracaoOtimaController {
    @FXML
    public TableView table;

    @FXML
    public TextField valor;

    @FXML
    public Button back;

    @FXML
    public TableView tablepacs;

    private static Configuracao configuracao;
    private Configuracao configuracaoGerada;

    public static void setConfiguracao(Configuracao newConfiguracao) {
        configuracao = newConfiguracao;
    }

    private CCH cch = CarConfiguratorHubApplication.getCch();
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



        ObservableList<TableColumn> observableListPacs = tablepacs.getColumns();

        observableListPacs.get(0).setCellValueFactory(
                new PropertyValueFactory<Pacote, String>("nome")
        );

        addComponenteButtonToTableColumn(observableListPacs.get(1));

        observableListPacs.get(2).setCellValueFactory(
                new PropertyValueFactory<Pacote, Integer>("desconto")
        );
        if(configuracaoGerada!=null){
            ObservableList<Componente> componentes = FXCollections.observableArrayList();
            ObservableList<Pacote> pacotes = FXCollections.observableArrayList();
            componentes.addAll(configuracaoGerada.componentesNotInPacotes().values());
            pacotes.addAll(configuracaoGerada.consultarPacotes().values());
            table.setItems(componentes);
            tablepacs.setItems(pacotes);
        }
        else{
            table.setItems(null);
            tablepacs.setItems(null);
        }
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

    @FXML
    private void loadConfiguracaoOtima() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        ObservableList<Pacote> pacotes = FXCollections.observableArrayList();
        double valorMaximo;

        try {
            valorMaximo = Double.parseDouble(valor.getText());
        } catch (NumberFormatException a) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Impossível obter configuração ótima");
            alert.setContentText("Insira um valor máximo.");
            alert.showAndWait();

            valorMaximo = -1;
        }

        try {
            configuracaoGerada = cch.ConfiguracaoOtima(configuracao, valorMaximo);
            componentes.addAll(configuracaoGerada.componentesNotInPacotes().values());
            pacotes.addAll(configuracaoGerada.consultarPacotes().values());
        } catch (NoOptimalConfigurationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText("");
            alert.setContentText("Não existe uma configuração ótima.");
            alert.showAndWait();
        }

        table.setItems(componentes);
        tablepacs.setItems(pacotes);
    }

    @FXML
    public void aplicar() {
        gestaoDeConfiguracao.removerConfiguracao(configuracao.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText("Configuração aplicada com sucesso!");
        alert.setContentText("Pode consultar a Configuração " + configuracaoGerada.getId() + ".");
        alert.showAndWait();
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
