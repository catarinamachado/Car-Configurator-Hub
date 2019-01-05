package CCH.dataaccess;

import java.util.List;

public interface RemoteClass<K> {

    K key();
    RemoteClass<K> fromRow(List<String> row);
    List<String> toRow();
    K key(String k);
}
