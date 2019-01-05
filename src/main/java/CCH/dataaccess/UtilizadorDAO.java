package CCH.dataaccess;

import CCH.business.Utilizador;

import java.sql.Statement;
import java.util.Arrays;

public class UtilizadorDAO extends GenericDAOClass<Integer> {


    public UtilizadorDAO () {
        super("Utilizador",
                new Utilizador(),
                Arrays.asList(new String[]{"id","nome","password","TipoUtilizador"}));
    }

    public Utilizador get(Object key) {
        return (Utilizador )super.get(key);
    }

    public Utilizador  put(Integer key, Utilizador  value){
        return (Utilizador )super.put(key,value);
    }

    public Utilizador  remove(Object key){
        return (Utilizador )super.remove(key);
    }

}
