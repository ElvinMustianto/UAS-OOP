import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    List<T> fetchAll() throws SQLException, ClassNotFoundException;
}
