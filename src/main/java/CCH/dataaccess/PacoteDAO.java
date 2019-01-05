package CCH.dataaccess;

import CCH.business.ClasseComponente;
import CCH.business.Componente;
import CCH.business.Pacote;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PacoteDAO extends GenericDAOClass<Integer> {

    private ComponenteDAO componenteDAO = new ComponenteDAO();
    private ClasseComponenteDAO classeComponenteDAO = new ClasseComponenteDAO();

    public PacoteDAO () {
        super("Pacote", new Pacote(),
                Arrays.asList(new String[]{"id","desconto"}));
    }

    public Pacote get(Object key) {
        return (Pacote)super.get(key);
    }

    public Pacote put(Integer key, Pacote value){
        return (Pacote)super.put(key,value);
    }

    public Pacote remove(Object key){
        return (Pacote)super.remove(key);
    }

    public void updateDesconto(Pacote pacote) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE Pacote SET desconto = " +
                    pacote.getDesconto() +
                    " WHERE id = " +
                    pacote.getId() +
                    ";");

        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public void removeAllComponentes(Object key) {
        try {
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM Pacote_has_Componente WHERE Pacote_id = " + key + ";";
            stm.executeUpdate(sql);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public double getDescontoPacote(Object key) {
        try {
            double al = 0.0;
            Statement stm = conn.createStatement();
            String sql = "SELECT desconto FROM Pacote WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                al = rs.getInt(1);
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}
