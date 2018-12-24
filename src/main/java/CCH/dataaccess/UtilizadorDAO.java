package CCH.dataaccess;

import CCH.business.*;

import java.util.*;
import java.sql.*;

public class UtilizadorDAO implements Map<Integer, Utilizador> {

    public Connection conn;

    public UtilizadorDAO () {
        conn = CCHConnection.getConnection();
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Utilizador WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Utilizador get(Object key) {
        try {
            Utilizador al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Utilizador WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                TipoUtilizador tipoUtilizador = TipoUtilizador.values()[rs.getInt(4)];
                al = new Utilizador(rs.getInt(1),rs.getString(2),rs.getString(3), tipoUtilizador);
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Utilizador put(Integer key, Utilizador value) {
        try {
            Statement stm = conn.createStatement();

            stm.executeUpdate("DELETE FROM Utilizador WHERE id='"+key+"'");
            String sql = "INSERT INTO Utilizador VALUES ('" +
                    value.getId() + "','" + value.getNome() + "','" + value.getPassword() +
                    "','" + value.getTipoUtilizador().getValue() +"');";

            int i  = stm.executeUpdate(sql);

            return get(key);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Utilizador remove(Object key) {
        try {
            Utilizador al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE " + key + " FROM Utilizador";
            int i  = stm.executeUpdate(sql);
            return al;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM Utilizador");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<Utilizador> values() {
        try {
            Collection<Utilizador> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador");

            while (rs.next()) {
                TipoUtilizador tipoUtilizador = TipoUtilizador.values()[rs.getInt(4)];
                Utilizador al = new Utilizador(rs.getInt(1),rs.getString(2),rs.getString(3), tipoUtilizador);
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, Utilizador> getAll() {
        Map<Integer, Utilizador> hashmap = new HashMap<>();
        Collection<Utilizador> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public int hashCode() {
        return this.conn.hashCode();
    }

    public boolean containsValue(Object value) {
        throw new NullPointerException("Not implemented!");
    }

    public Set<Map.Entry<Integer, Utilizador>> entrySet() {
        throw new NullPointerException("Not implemented!");    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");    }

    public void putAll(Map<? extends Integer,? extends Utilizador> t) {
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

