package CCH.dataaccess;

import CCH.business.Componente;
import CCH.business.Encomenda;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class EncomendaDAO extends GenericDAOClass<Integer> {


    public EncomendaDAO () {
        super("Encomenda",
                new Encomenda(),
                Arrays.asList(new String[]{"id","nomeCliente",
                        "numeroDeIdentificaoCliente","moradaCliente","paisCliente",
                        "emailCliente","emailCliente","preco"}));
    }

    public Encomenda get(Object key) {
        return (Encomenda)super.get(key);
    }

    public Map<Integer,Encomenda> getAllEncomenda() {
        Map<Integer, RemoteClass<Integer>> a = super.getAll();
        Map<Integer, Encomenda> r = new HashMap<>();
        a.forEach((k,v) -> r.put(k, (Encomenda) v));
        return r;
    }

    public Encomenda put(Integer key, Encomenda value){
        return (Encomenda)super.put(key,value);
    }

    public Encomenda remove(Object key){
        return (Encomenda)super.remove(key);
    }

    public Map<Integer, Componente> getComponentes(Integer encomendaId) {
        try {
            Map<Integer, Componente> componentes = new HashMap<>();
            Statement stm = conn.createStatement();
            String sql = "SELECT C.* FROM Encomenda_has_Componente as EC " +
                    ", Componente as C WHERE Encomenda_id=" + encomendaId + " and EC.componente_id = C.id;";
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

    public void putComponente (int idEncomenda, int idComponente) {
        try {
            Statement stm = conn.createStatement();
            String sql = "INSERT INTO Encomenda_has_Componente VALUES ('" +
                    idEncomenda + "','" + idComponente + "');";
            stm.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
}
