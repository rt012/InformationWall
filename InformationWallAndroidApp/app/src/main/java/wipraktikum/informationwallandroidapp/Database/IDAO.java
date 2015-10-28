package wipraktikum.informationwallandroidapp.Database;

import java.util.ArrayList;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public interface IDAO {
    public ArrayList queryForAll();
    public Object queryForId(long iD);
    public void create(Object object);
    public void delete(Object object);
    public void deleteByID(long Id);
    Object copyObject(E dbObject);
}
