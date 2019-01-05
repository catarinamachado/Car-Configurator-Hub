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

    public int getNextId() {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Pacote ORDER BY id DESC LIMIT 1;";
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }

            return 0;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Pacote WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
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

    public Map<Integer, Componente> getComponentes(Integer pacoteId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Pacote_has_Componente WHERE Pacote_id=" + pacoteId;
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Componente componente = this.componenteDAO.get(rs.getInt(2));
                componentes.put(componente.getId(), componente);
            }

            return componentes;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Collection<Componente> getAllComponentesNoPacote(Object key) {
        try {
            Collection<Componente> col = new HashSet<>();
            Componente al = null;
            Statement stm = conn.createStatement();

            ResultSet rs_componenteId = stm.executeQuery("SELECT Componente_id FROM Pacote_has_Componente where Pacote_id = " + key);

            while (rs_componenteId.next()) {
                Object componente_id = rs_componenteId.getInt(1);
                String sql = "SELECT * FROM Componente WHERE id=" + componente_id;
                stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);

                if (rs.next()) {
                    ClasseComponente classeComponente = classeComponenteDAO.get(rs.getInt(5));
                    al = new Componente(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getString(4), classeComponente);

                    col.add(al);
                }
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}

    }

    public Collection<Integer> getAllIdsComponentesNoPacote(Object key) {
        try {
            Collection<Integer> col = new HashSet<>();
            Statement stm = conn.createStatement();

            ResultSet rs_componenteId = stm.executeQuery("SELECT Componente_id FROM Pacote_has_Componente where Pacote_id = " + key);

            while (rs_componenteId.next()) {
                Object componente_id = rs_componenteId.getInt(1);
                String sql = "SELECT id FROM Componente WHERE id=" + componente_id;
                stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);

                if (rs.next()) {
                    col.add(rs.getInt(1));
                }
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}

    }

    public void removeComponente(Object key_pacote, Object key_componente) {
        try {
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM Pacote_has_Componente WHERE Pacote_id = " + key_pacote + " and " + "Componente_id = " + key_componente;
            stm.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public void adicionaComponente(Object key_pacote, Object key_componente) {
        try {
            Statement stm = conn.createStatement();
            String sql = "INSERT Pacote_has_Componente VALUES (" + key_pacote + ", " + key_componente + ");";
            stm.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
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
