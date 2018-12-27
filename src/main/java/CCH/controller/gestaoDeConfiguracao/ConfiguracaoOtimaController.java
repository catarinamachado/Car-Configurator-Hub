package CCH.controller.gestaoDeConfiguracao;

import CCH.business.CCH;
import CCH.business.Componente;
import CCH.business.Configuracao;
import CCH.business.GestaoDeConfiguracao;
import CCH.exception.NoOptimalConfigurationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import CCH.CarConfiguratorHubApplication;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ConfiguracaoOtimaController {
    @FXML
    public TableView table;

    @FXML
    public TextField valor;

    @FXML
    public Button back;

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

        table.setItems(null);
    }

    @FXML
    private void loadConfiguracaoOtima() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
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
            componentes.addAll(configuracaoGerada.consultarComponentes().values());
        } catch (NoOptimalConfigurationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText("");
            alert.setContentText("Não existe uma configuração ótima.");
            alert.showAndWait();
        }

        table.setItems(componentes);
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
