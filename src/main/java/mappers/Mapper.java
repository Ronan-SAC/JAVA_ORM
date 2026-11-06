package mappers;

import java.sql.ResultSet;
import java.util.HashMap;

public interface Mapper<T> {
    T toEntity(ResultSet rs);
    HashMap<String, Object> toMap(T object);
}
