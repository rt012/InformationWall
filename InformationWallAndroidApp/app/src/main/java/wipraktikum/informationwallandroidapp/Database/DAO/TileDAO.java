package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBTile;
import wipraktikum.informationwallandroidapp.Database.IDAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class TileDAO implements IDAO{
    @Override
    public ArrayList queryForAll() {
        try {
            List<DBTile> dbTiles = InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().queryForAll();
            List<Tile> tiles = null;
            for(DBTile dbTile : tiles){


                tiles.add();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object queryForId(long iD) {
        return null;
    }

    @Override
    public void create(Object object) {

    }

    @Override
    public void delete(Object object) {

    }

    @Override
    public void deleteByID(long Id) {

    }

    @Override
    public Object copyObject(DBTile dbTile) {
        Tile tile = new Tile();
        tile.setTileSize(dbTile.getTileSize());
        tile.setIsActivated(dbTile.getIsActivated());

        return null;
    }
}
