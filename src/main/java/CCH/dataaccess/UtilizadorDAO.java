package CCH.dataaccess;

import CCH.business.Utilizador;

import java.sql.Statement;
import java.util.Arrays;

public class UtilizadorDAO extends GenericDAOClass<Integer> {


    public UtilizadorDAO () {
        super("Utilizador",
                new Utilizador(),
                Arrays.asList(new String[]{"id","nome","password","TipoUtilizador"}));
    }

    public Utilizador get(Object key) {
        return (Utilizador )super.get(key);
    }

    public Utilizador  put(Integer key, Utilizador  value){
        return (Utilizador )super.put(key,value);
    }

    public Utilizador  remove(Object key){
        return (Utilizador )super.remove(key);
    }

    public void updateUser(Utilizador utilizador) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE Utilizador SET nome = '" +
                    utilizador.getNome() +
                    "' WHERE id = " +
                    utilizador.getId() +
                    ";");

        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public void updatePassword(Utilizador utilizador) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE Utilizador SET password = '" +
                    utilizador.getPassword() +
                    "' WHERE id = " +
                    utilizador.getId() +
                    ";");
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public void updateTipo(Utilizador utilizador) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE Utilizador SET TipoUtilizador = " +
                    utilizador.getTipoUtilizador().getValue() +
                    " WHERE id = " +
                    utilizador.getId() +
                    ";");

        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

}
