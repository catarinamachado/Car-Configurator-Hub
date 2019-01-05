package CCH.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class GenericDAOClass<K> implements Map<K, RemoteClass<K>> {

    public final Connection conn = CCHConnection.getConnection();
    private final String tablename;
    private final RemoteClass<K> token;
    private final List<String> colname;


    public GenericDAOClass (String tablename, RemoteClass<K> token, List<String> colname) {
        this.tablename = tablename;
        this.token = token;
        this.colname = colname;
    }

    public int getNextId() {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT id FROM " + this.tablename + " ORDER BY id DESC LIMIT 1;";
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }

            return 0;
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }

    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT " + this.colname.get(0) + " FROM " +
                    this.tablename + " WHERE " +
                        this.colname.get(0) + " = '" + key + "' ;";
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }

    public RemoteClass<K> get(Object key) {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM " + this.tablename + " WHERE " + this.colname.get(0) + " = '" + key + "' ;";
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                List<String> row = new LinkedList<>();
                int col = rs.getMetaData().getColumnCount();
                for(int i = 1; i <= col; i++)
                    row.add(rs.getString(i));

                return token.fromRow(row);
            }

            return null;
        }
        catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public RemoteClass<K> update(K key, RemoteClass<K> value){
        try {

            List<String> row = value.toRow();

            Statement stm = conn.createStatement();
            StringBuilder sql = new StringBuilder("UPDATE " + this.tablename + " SET ");


            for(int i = 1; i < (this.colname.size()-1) ; i++){
                sql.append(this.colname.get(i) + " = '" + row.get(i) +"' , ");
            }

            sql.append(this.colname.get(this.colname.size()-1) + " = " +
                    row.get(this.colname.size()-1) + " WHERE " +
                        this.colname.get(0) + " = '" + key + "';");

            stm.executeUpdate(sql.toString());

            return value;
        }
        catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        }

    }

    public RemoteClass<K> put(K key, RemoteClass<K> value) {
        try {
            Statement stm = conn.createStatement();

            RemoteClass<K> result = this.get(key);

            this.remove(key);

            StringBuilder sql = new StringBuilder("INSERT INTO " + this.tablename + " VALUES (");

            List<String> l = value.toRow();

            for( int i = 0; i < (l.size()-1); i++ ) {
                sql.append("'" + l.get(i) + "'" + ",");
            }
            sql.append("'" + l.get(l.size()-1) + "');");

            stm.executeUpdate(sql.toString());

            return result;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public RemoteClass<K> remove(Object key) {
        try {
            RemoteClass<K> al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "DELETE FROM " + this.tablename + " WHERE " + this.colname.get(0) + " = '" + key + "' ;";
            int i  = stm.executeUpdate(sql);
            return al;
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }

    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM " + this.tablename + " ;");

            return Integer.valueOf(rs.getString(1));

        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }

    public Collection<RemoteClass<K>> values() {
        try {
            Collection<RemoteClass<K>> set = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM " + this.tablename + " ;");

            List<String> row;
            int col = rs.getMetaData().getColumnCount();


            while (rs.next()) {

                row = new ArrayList<>();
                for( int i = 1; i<= col; i++)
                    row.add(rs.getString(i));
                set.add( this.token.fromRow(row) );
            }

            return set;
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }

    public Map<K, RemoteClass<K>> getAll() {
        Map<K, RemoteClass<K>> hashmap = new HashMap<>();
        Collection<RemoteClass<K>> collection = values();

        collection.forEach(u -> hashmap.put(u.key(), u));

        return hashmap;
    }

    public boolean containsValue(Object value) {

        if(!(value instanceof RemoteClass))
            return false;

        try {

            RemoteClass<K> other = (RemoteClass<K>)value;

            List<String> row = other.toRow();

            Statement stm = conn.createStatement();
            StringBuilder sql = new StringBuilder("SELECT * FROM " + this.tablename + " WHERE ");


            for(int i = 0; i < (this.colname.size()-1) ; i++){
               sql.append(this.colname.get(i) + " = '" + row.get(i) + "' and ");
            }

            sql.append(this.colname.get(this.colname.size()-1) + " = '" + row.get(this.colname.size()-1) + "' ; ");

            ResultSet rs = stm.executeQuery(sql.toString());

            return rs.next();
        }
        catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public Set<Entry<K, RemoteClass<K>>> entrySet() {

        try {
            Set<Entry<K, RemoteClass<K>>> set = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM " + this.tablename + " ;");

            List<String> row;
            int col = rs.getMetaData().getColumnCount();


            while (rs.next()) {

                row = new ArrayList<>();
                for( int i = 1; i<= col; i++)
                    row.add(rs.getString(i));

                RemoteClass<K> r = this.token.fromRow(row);

                set.add(new AbstractMap.SimpleEntry(r.key(),r));
            }

            return set;
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}

    }

    public boolean equals(Object o) {
        return o.getClass().equals( this.getClass());
    }

    public void putAll(Map<? extends K,? extends RemoteClass<K>> t) {
        for( Map.Entry<? extends K,? extends RemoteClass<K>> d : t.entrySet() )
            if(this.token.getClass().equals(d.getValue().getClass()))
                this.put(d.getKey(),d.getValue());

    }

    public void clear () {
        throw new NullPointerException("Not implemented yet!");
    }

    public boolean isEmpty() {
        return (this.size() == 0 );
    }

    public Set<K> keySet() {
        try {

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT " + this.colname.get(0) + " FROM " + this.tablename + " ;");

            Set<K> set = new HashSet<>();
            while (rs.next()) {
                set.add( this.token.key(rs.getString(1))  );
            }

            return set;

        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
    }
}
