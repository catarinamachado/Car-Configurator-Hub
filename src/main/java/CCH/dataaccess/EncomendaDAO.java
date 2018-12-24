package CCH.dataaccess;

import CCH.business.Componente;
import CCH.business.Encomenda;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class EncomendaDAO implements Map<Integer, Encomenda> {

    public Connection conn;

    public EncomendaDAO () {
        conn = CCHConnection.getConnection();
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Encomenda WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Encomenda get(Object key) {
        try {
            Encomenda al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Encomenda WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                al = new Encomenda(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6));
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Encomenda put(Integer key, Encomenda value) {
        try {
            Statement stm = conn.createStatement();

            stm.executeUpdate("DELETE FROM Encomenda WHERE id='"+key+"'");
            String sql = "INSERT INTO Encomenda VALUES ('" +
                    value.getId() + "','" + value.getNomeCliente() + "','" + value.getNumeroDeIdentificacaoCliente() +
                    "','" + value.getMoradaCliente() + "','" + value.getPaisCliente() +
                    "','" + value.getEmailCliente() +
                    "','" + value.getId() + "');";

            int i  = stm.executeUpdate(sql);

            return get(key);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Encomenda remove(Object key) {
        try {
            Encomenda al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE " + key + " FROM Encomenda";
            int i  = stm.executeUpdate(sql);
            return al;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM Encomenda");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<Encomenda> values() {
        try {
            Collection<Encomenda> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Encomenda");

            while (rs.next()) {
                Encomenda al = new Encomenda(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6));
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, Encomenda> getAll() {
        Map<Integer, Encomenda> hashmap = new HashMap<>();
        Collection<Encomenda> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public Map<Integer, Componente> getComponentes(Integer configuracaoId) {
        try {
            Map<Integer, Componente> componentes = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Encomenda_has_Componente WHERE Encomenda_id=" + configuracaoId;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                Componente componente = new ComponenteDAO().get(rs.getInt(2));
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

    public Set<Entry<Integer, Encomenda>> entrySet() {
        throw new NullPointerException("Not implemented!");    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");    }

    public void putAll(Map<? extends Integer,? extends Encomenda> t) {
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
