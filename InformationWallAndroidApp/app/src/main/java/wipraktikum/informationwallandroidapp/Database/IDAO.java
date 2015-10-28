package wipraktikum.informationwallandroidapp.Database;

import java.util.ArrayList;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public interface IDAO {
    public ArrayList queryForAll();
    public Object queryForId(long iD);
    public boolean create(Object object);
    public boolean delete(Object object);
    public boolean deleteByID(long Id);
}
