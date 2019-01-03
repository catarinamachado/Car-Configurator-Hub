package CCH.dataaccess;

import CCH.business.ClasseComponente;
import CCH.business.TipoComponente;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClasseComponenteDAO implements Map<Integer, ClasseComponente> {

    public Connection conn;

    public ClasseComponenteDAO () {
        conn = CCHConnection.getConnection();
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM ClasseComponente WHERE ID = " + key;
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public ClasseComponente get(Object key) {
        try {
            ClasseComponente al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM ClasseComponente WHERE id=" + key;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                TipoComponente tipoComponente = TipoComponente.withValue(rs.getInt(4));
                al = new ClasseComponente(rs.getInt(1),rs.getBoolean(2),rs.getString(3), tipoComponente);
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
            ResultSet rs = stm.executeQuery("SELECT id FROM ClasseComponente");

            while (rs.next()) {
                i++;
            }

            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<ClasseComponente> values() {
        try {
            Collection<ClasseComponente> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM ClasseComponente");

            while (rs.next()) {
                TipoComponente tipoComponente = TipoComponente.withValue(rs.getInt(4));
                ClasseComponente al = new ClasseComponente(rs.getInt(1),rs.getBoolean(2),rs.getString(3), tipoComponente);
                col.add(al);
            }

            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<Integer, ClasseComponente> getAll() {
        Map<Integer, ClasseComponente> hashmap = new HashMap<>();
        Collection<ClasseComponente> collection = values();

        collection.forEach(u -> hashmap.put(u.getId(), u));

        return hashmap;
    }

    public ClasseComponente put(Integer key, ClasseComponente value) {
        throw new NullPointerException("Not implemented!");
    }

    public ClasseComponente remove(Object key) {
        throw new NullPointerException("Not implemented!");
    }

    public int hashCode() {
        return this.conn.hashCode();
    }

    public boolean containsValue(Object value) {
        throw new NullPointerException("Not implemented!");
    }

    public Set<Map.Entry<Integer, ClasseComponente>> entrySet() {
        throw new NullPointerException("Not implemented!");    }

    public boolean equals(Object o) {
        throw new NullPointerException("Not implemented!");    }

    public void putAll(Map<? extends Integer,? extends ClasseComponente> t) {
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
