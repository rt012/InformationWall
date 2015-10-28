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
        ArrayList<Tile> tiles = null;
        try {
            List<DBTile> dbTiles = InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().queryForAll();
            for(DBTile dbTile : dbTiles){
                tiles.add(mappDBTiletoTile(dbTile));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiles;
    }

    @Override
    public Object queryForId(long iD) {
        Tile tile = null;
        try {
            DBTile dbTile = InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().queryForId(iD);
            tile = mappDBTiletoTile(dbTile);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tile;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().create(mappTiletoDBTile((Tile) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        boolean ok = false;
        // TODO falls nötig
        return ok;
    }

    @Override
    public boolean deleteByID(long Id) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }


    public Tile mappDBTiletoTile(DBTile dbTile) {

        Tile tile = new Tile();
        tile.setTileSize(dbTile.getTileSize());
        tile.setIsActivated(dbTile.getIsActivated());
        tile.setmDrawableId(dbTile.getDrawableId());
        tile.setmName(dbTile.getName());
        tile.setmTileID(dbTile.getTileID());

        return tile;
    }
    public DBTile mappTiletoDBTile(Tile tile) {

        DBTile dbTile = new DBTile();
        dbTile.setTileSize(tile.getTileSize());
        dbTile.setIsActivated(tile.getIsActivated());
        dbTile.setmDrawableId(tile.getDrawableId());
        dbTile.setmName(tile.getName());


        return dbTile;
    }
}
