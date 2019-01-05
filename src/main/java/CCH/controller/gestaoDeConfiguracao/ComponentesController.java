package CCH.controller.gestaoDeConfiguracao;

import CCH.CarConfiguratorHubApplication;

import CCH.business.CCH;
import CCH.business.Componente;
import CCH.business.Configuracao;
import CCH.exception.ComponenteJaAdicionadoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.lang.StringBuilder;
import java.util.*;


public class ComponentesController {
    @FXML
    public TableView table;

    @FXML
    public Button back;

    private CCH cch = CarConfiguratorHubApplication.getCch();

    private static Configuracao configuracao;
    public static void setConfiguracao(Configuracao newConfiguracao) {
        configuracao = newConfiguracao;
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

        table.setItems(getComponentes());

        setSelection();
    }

    private ObservableList<Componente> getComponentes() {
        ObservableList<Componente> componentes = FXCollections.observableArrayList();
        componentes.addAll(cch.consultarComponentes());
        return componentes;
    }


    private void setSelection() {
        table.setRowFactory(tv -> {
            TableRow<Componente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Componente novoComponente = null;
                try {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        novoComponente = row.getItem();
                        Map<Integer,Componente> comps = new HashMap<>();
                        comps.put(novoComponente.getId(), novoComponente);

                        List<Componente> incompativeis = configuracao.componentesIncompativeisNaConfig(comps);
                        List<Componente> requeridos = configuracao.componentesRequeridosQueNaoEstaoConfig(comps);
                        boolean flag = true;
                        boolean adquiriu = true;

                        if (incompativeis.size() != 0) {
                            flag = temProblemas("i", incompativeis);
                            if (flag && requeridos.size() != 0)
                                flag = temProblemas("r", requeridos);
                        } else if (requeridos.size() != 0)
                            flag = temProblemas("r", requeridos);

                        if (flag) {
                            configuracao.adicionarComponente(novoComponente.getId());
                            adquiriu = configuracao.checkforPacotesInConfiguration();
                        }

                        if (adquiriu) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Informação");
                            alert.setHeaderText("Pacote adquirido!");
                            alert.setContentText("O componente que adicionou formou um pacote na configuração.");

                            alert.showAndWait();
                        }

                        ((Stage) back.getScene().getWindow()).close();
                    }
                } catch (ComponenteJaAdicionadoException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Componente já adicionado");
                    alert.setContentText("Esta configuração já contém o " + novoComponente.getFullName() + ".");

                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return row;
        });
    }

    public boolean temProblemas(String tipo, List<Componente> componentes) {
        StringBuilder str = new StringBuilder("Componente: ");

        int i = 0;
        for(i = 0; i < componentes.size() - 1 ; i++) {
            str.append(componentes.get(i).getFullName() + ", ");
        }
        str.append(componentes.get(i).getFullName() + ".");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        if (tipo.equals("i")) {
            alert.setHeaderText("O componente que pretende adicionar é incompatível com o " +
                    str);
        } else {
            alert.setHeaderText("O componente que pretende adicionar requer o " +
                    str);
        }
        alert.setContentText("Pretende adicionar o componente na mesma?");

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
