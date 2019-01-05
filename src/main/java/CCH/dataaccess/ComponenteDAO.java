package CCH.dataaccess;

import CCH.business.Componente;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ComponenteDAO extends GenericDAOClass<Integer> {

    public ComponenteDAO () {
        super("Componente",
                new Componente(),
                Arrays.asList(new String[]{"id","stock","preco","nome","ClasseComponente_id"}));
    }

    public Componente get(Object key) {
        return (Componente)super.get(key);
    }

    public Componente put(Integer key, Componente value){
        return (Componente)super.put(key,value);
    }

    public Componente remove(Object key){
        return (Componente)super.remove(key);
    }

    public Map<Integer,Componente> getAllComponente() {
        Map<Integer, RemoteClass<Integer>> a = super.getAll();
        Map<Integer, Componente> r = new HashMap<>();
        a.forEach((k,v) -> r.put(k, (Componente) v));
        return r;
    }

    public Map<Integer, Componente> getComponentesIncompativeis(Integer componenteId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();

            String sql = "SELECT C.* FROM Componente_incompativel_Componente as CC, Componente as C WHERE " +
                    "(CC.Componente_id=" + componenteId + " and C.id = CC.Componente_id1) OR " +
                    "(CC.Componente_id = C.id and CC.Componente_id1=" + componenteId + ");";

            ResultSet rs = stm.executeQuery(sql);

            List<String> row;
            int col = rs.getMetaData().getColumnCount();
            Componente token = new Componente();

            while (rs.next()) {

                row = new ArrayList<>();
                for( int i = 1; i<= col; i++)
                    row.add(rs.getString(i));

                Componente n = token.fromRow(row);
                componentes.put(n.key(),n);
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
            String sql = "SELECT C.* FROM Componente_requer_Componente as CC," +
                    " Componente as C WHERE CC.Componente_id=" + componenteId + " and CC.Componente_id1 = C.id ;";
            ResultSet rs = stm.executeQuery(sql);

            List<String> row;
            int col = rs.getMetaData().getColumnCount();
            Componente token = new Componente();

            while (rs.next()) {

                row = new ArrayList<>();
                for( int i = 1; i<= col; i++)
                    row.add(rs.getString(i));

                Componente n = token.fromRow(row);
                componentes.put(n.key(),n);
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

}
