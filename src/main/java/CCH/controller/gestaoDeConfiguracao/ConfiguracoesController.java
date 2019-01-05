package CCH.controller.gestaoDeConfiguracao;

import CCH.CarConfiguratorHubApplication;
import CCH.business.CCH;
import CCH.business.Configuracao;
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

import java.util.Optional;

public class ConfiguracoesController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private CCH cch = CarConfiguratorHubApplication.getCch();

    @FXML
    public void initialize() {
        ObservableList<TableColumn> observableList = table.getColumns();

        observableList.get(0).setCellValueFactory(
                new PropertyValueFactory<Configuracao, String>("nome")
        );


        observableList.get(1).setCellValueFactory(
                new PropertyValueFactory<Configuracao, Double>("precoFinal")
        );

        addDeleteButtonToTableColumn(observableList.get(2));

        table.setItems(getConfiguracoes());
        setSelection();
        table.refresh();
    }

    private void setSelection() {
        table.setRowFactory(tv -> {
            TableRow<Configuracao> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                try {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Configuracao configuracao = row.getItem();

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/gestaoDeConfiguracao/configuracao.fxml"));
                        ConfiguracaoController.setConfiguracao(configuracao);
                        Parent root = fxmlLoader.load();

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.initOwner(back.getScene().getWindow());
                        stage.setScene(scene);

                        stage.showAndWait();
                        initialize();
                    }
                } catch (Exception e) { }
            });
            return row;
        });
    }

    private ObservableList<Configuracao> getConfiguracoes() {
        ObservableList<Configuracao> configuracoes = FXCollections.observableArrayList();
        configuracoes.addAll(cch.consultarConfiguracoes());
        return configuracoes;
    }

    private void addDeleteButtonToTableColumn(TableColumn t) {
        Callback<TableColumn<Configuracao, Void>, TableCell<Configuracao, Void>> cellFactory = new Callback<TableColumn<Configuracao, Void>, TableCell<Configuracao, Void>>() {
            @Override
            public TableCell<Configuracao, Void> call(final TableColumn<Configuracao, Void> param) {
                final TableCell<Configuracao, Void> cell = new TableCell<Configuracao, Void>() {

                    private final Button btn = new Button("Apagar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configuracao configuracao = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Aviso");
                            alert.setHeaderText("A " + configuracao.getNome() + " será permanentemente apagado.");
                            alert.setContentText("Pretende continuar?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                cch.removerConfiguracao(configuracao.getId());
                                table.setItems(getConfiguracoes());
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
    public void add() {
        cch.criarConfiguracao();
        table.setItems(getConfiguracoes());
        table.refresh();
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
