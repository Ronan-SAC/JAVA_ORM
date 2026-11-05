package mappers;

import java.sql.ResultSet;
import java.util.HashMap;

public interface Mapper<T> {
    T paraEntidade(ResultSet rs);
    HashMap<String, Object> paraMap(T objeto);
}
