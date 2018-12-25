package CCH.dataaccess;

import CCH.business.Componente;
import CCH.business.Pacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PacoteDAO implements Map<Integer, Pacote> {

    public Connection conn;

    private ComponenteDAO componenteDAO = new ComponenteDAO();

    public PacoteDAO () {
        conn = CCHConnection.getConnection();
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
}
