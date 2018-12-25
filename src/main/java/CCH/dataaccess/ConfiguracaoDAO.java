package CCH.dataaccess;

import CCH.business.Componente;
import CCH.business.Configuracao;
import CCH.business.Pacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ConfiguracaoDAO implements Map<Integer, Configuracao> {

    public Connection conn;

    private PacoteDAO pacoteDAO = new PacoteDAO();
    private ComponenteDAO componenteDAO = new ComponenteDAO();

    public ConfiguracaoDAO () {
        conn = CCHConnection.getConnection();
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM Configuracao WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Configuracao get(Object key) {
        try {
            Configuracao al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Configuracao WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                al = new Configuracao(rs.getInt(1),rs.getDouble(2),rs.getDouble(3));
            }

            return al;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Configuracao put(Integer key, Configuracao value) {
        try {
            Statement stm = conn.createStatement();

            stm.executeUpdate("DELETE FROM Configuracao WHERE id='"+key+"'");

            String sql = "INSERT INTO Configuracao VALUES ('" + value.getId() +
                    "','" + value.getPreco() + "','" + value.getDesconto() + "');";

            int i  = stm.executeUpdate(sql);

            return get(key);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Configuracao remove(Object key) {
        try {
            Configuracao al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM Configuracao WHERE id = " + key;
            int i  = stm.executeUpdate(sql);
            return al;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM Configuracao");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<Configuracao> values() {
        try {
            Collection<Configuracao> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Configuracao");

            while (rs.next()) {
                Configuracao al = new Configuracao(rs.getInt(1),rs.getDouble(2),rs.getDouble(3));
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, Configuracao> getAll() {
        Map<Integer, Configuracao> hashmap = new HashMap<>();
        Collection<Configuracao> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public Map<Integer, Pacote> getPacotes(Integer configuracaoId) {
        try {
            Map<Integer, Pacote> pacotes = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Configuracao_has_Pacote WHERE Configuracao_id=" + configuracaoId;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                Pacote pacote = pacoteDAO.get(rs.getInt(2));
                pacotes.put(pacote.getId(), pacote);
            }

            return pacotes;
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Map<Integer, Componente> getComponentes(Integer configuracaoId) {
        try {
            Map<Integer, Componente> componentes = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Configuracao_has_Componente WHERE Pacote_id=" + configuracaoId;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
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

    public Set<Entry<Integer, Configuracao>> entrySet() {
        throw new NullPointerException("Not implemented!");    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");    }

    public void putAll(Map<? extends Integer,? extends Configuracao> t) {
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
