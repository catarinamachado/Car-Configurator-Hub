package CCH.controller.gestaoDeConfiguracao;

import CCH.CarConfiguratorHubApplication;
import CCH.business.CCH;
import CCH.business.Configuracao;

import CCH.business.GestaoDeConfiguracao;
import CCH.exception.EncomendaRequerObrigatoriosException;
import CCH.exception.EncomendaRequerOutrosComponentes;
import CCH.exception.EncomendaTemComponentesIncompativeis;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EncomendaController {
    @FXML public Button back;
    @FXML public TextField nome;
    @FXML public TextField id;
    @FXML public TextField morada;
    @FXML public TextField pais;
    @FXML public TextField email;

    /*//private static Configuracao configuracao;
    public static void setConfiguracao(Configuracao newConfiguracao) {
        configuracao = newConfiguracao;
    }
    */

    private CCH cch = CarConfiguratorHubApplication.getCch();

    @FXML
    public void criarEncomenda() {
        try {
            String cname = cch.getConfigAtual().getNome();
            Double preco = cch.getConfigAtual().getPreco();
            cch.criarEncomenda(
                    nome.getText(),
                    id.getText(),
                    morada.getText(),
                    pais.getText(),
                    email.getText()
            );

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("A Encomenda foi criada com sucesso");
            alert.setContentText(cname + ", " + preco + "€");

            alert.showAndWait();
        } catch (EncomendaRequerOutrosComponentes e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Outros componentes são necessários!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        } catch (EncomendaTemComponentesIncompativeis e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Alguns componentes são incompatíveis!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        } catch (EncomendaRequerObrigatoriosException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Encomenda requer Componentes Obrigatórios!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    @FXML
    public void back() {
        ((Stage) back.getScene().getWindow()).close();
    }
}
