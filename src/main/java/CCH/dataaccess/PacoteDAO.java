package CCH.dataaccess;

import CCH.business.ClasseComponente;
import CCH.business.Componente;
import CCH.business.Pacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PacoteDAO implements Map<Integer, Pacote> {

    public Connection conn;

    private ComponenteDAO componenteDAO = new ComponenteDAO();
    private ClasseComponenteDAO classeComponenteDAO = new ClasseComponenteDAO();

    public PacoteDAO () {
        conn = CCHConnection.getConnection();
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
        try {
            Pacote al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Pacote WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                al = new Pacote(rs.getInt(1),rs.getDouble(2));
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Pacote put(Integer key, Pacote value) {
        try {
            Statement stm = conn.createStatement();

            stm.executeUpdate("DELETE FROM Pacote WHERE id='"+key+"'");
            String sql = "INSERT INTO Pacote VALUES ('" +
                    value.getId() + "','" + value.getDesconto() +"');";

            int i  = stm.executeUpdate(sql);

            return get(key);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Pacote remove(Object key) {
        try {
            Pacote al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM Pacote WHERE id = " + key;
            int i  = stm.executeUpdate(sql);
            return al;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM Pacote");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<Pacote> values() {
        try {
            Collection<Pacote> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Pacote");

            while (rs.next()) {
                Pacote al = new Pacote(rs.getInt(1),rs.getDouble(2));
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, Pacote> getAll() {
        Map<Integer, Pacote> hashmap = new HashMap<>();
        Collection<Pacote> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public Map<Integer, Componente> getComponentes(Integer pacoteId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Pacote_has_Componente WHERE Pacote_id=" + pacoteId;
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Componente componente = componenteDAO.get(rs.getInt(2));
                componentes.put(componente.getId(), componente);
            }

            return componentes;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public int hashCode() {
        return this.conn.hashCode();
    }

    public boolean containsValue(Object value) {
        throw new NullPointerException("Not implemented!");
    }

    public Set<Map.Entry<Integer, Pacote>> entrySet() {
        throw new NullPointerException("Not implemented!");    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");    }

    public void putAll(Map<? extends Integer,? extends Pacote> t) {
        throw new NullPointerException("Not implemented!");
    }

    public void clear () {
        throw new NullPointerException("Not implemented!");
    }

    public boolean isEmpty() {
        throw new NullPointerException("Not implemented!");
    }

    public Set<Integer> keySet() {
        throw new NullPointerException("Not implemented!");
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

    public Pacote removeAllComponentes(Object key) {
        try {
            Pacote al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM Pacote_has_Componente WHERE Pacote_id = " + key + ";";
            stm.executeUpdate(sql);
            return al;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

}
