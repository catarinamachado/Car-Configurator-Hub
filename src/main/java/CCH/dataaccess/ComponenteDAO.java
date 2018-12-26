package CCH.dataaccess;

import CCH.business.ClasseComponente;
import CCH.business.Componente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ComponenteDAO implements Map<Integer, Componente> {

    public Connection conn;

    private ClasseComponenteDAO classeComponenteDAO = new ClasseComponenteDAO();

    public ComponenteDAO () {
        conn = CCHConnection.getConnection();
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Componente WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Componente get(Object key) {
        try {
            Componente al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Componente WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                ClasseComponente classeComponente = classeComponenteDAO.get(rs.getInt(5));
                al = new Componente(rs.getInt(1),rs.getInt(2),rs.getDouble(3), rs.getString(4), classeComponente);
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM Componente");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<Componente> values() {
        try {
            Collection<Componente> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Componente");

            while (rs.next()) {
                ClasseComponente classeComponente = classeComponenteDAO.get(rs.getInt(5));
                Componente al = new Componente(rs.getInt(1),rs.getInt(2),rs.getDouble(3), rs.getString(4), classeComponente);
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, Componente> getAll() {
        Map<Integer, Componente> hashmap = new HashMap<>();
        Collection<Componente> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public Map<Integer, Componente> getComponentesIncompativeis(Integer componenteId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();

            String sql = "SELECT * FROM Componente_incompativel_Componente WHERE " +
                    "Componente_id=" + componenteId + " OR " +
                    "Componente_id1=" + componenteId + ";";

            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                Componente componente = get(rs.getInt(2));
                componentes.put(componente.getId(), componente);
            }

            return componentes;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Map<Integer, Componente> getComponentesRequeridos(Integer componenteId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Componente_requer_Componente WHERE Componente_id=" + componenteId;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                Componente componente = get(rs.getInt(2));
                componentes.put(componente.getId(), componente);
            }

            return componentes;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Componente updateStock(Componente componente) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE Componente SET stock = " +
                    componente.getStock() +
                    " WHERE id = " +
                    componente.getId() +
                    ";");

            return get(componente.getId());
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Componente put(Integer key, Componente value) {
        throw new NullPointerException("Not implemented!");

    }

    public Componente remove(Object key) {
        throw new NullPointerException("Not implemented!");
    }

    public int hashCode() {
        return this.conn.hashCode();
    }

    public boolean containsValue(Object value) {
        throw new NullPointerException("Not implemented!");
    }

    public Set<Entry<Integer, Componente>> entrySet() {
        throw new NullPointerException("Not implemented!");
    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");
    }

    public void putAll(Map<? extends Integer,? extends Componente> t) {
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
}
