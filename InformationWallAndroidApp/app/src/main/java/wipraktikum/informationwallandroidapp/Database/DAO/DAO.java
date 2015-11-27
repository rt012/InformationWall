package wipraktikum.informationwallandroidapp.Database.DAO;

import java.util.ArrayList;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public interface DAO {
    ArrayList queryForAll();
    Object queryForId(long iD);
    boolean create(Object object);
    boolean update(Object object);
    boolean delete(Object object);
    boolean deleteByID(long Id);
}
