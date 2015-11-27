package wipraktikum.informationwallandroidapp.Database.DAO.Tile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.Tile.DBTile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class TileDAO implements DAO {
    private static TileDAO instance = null;

    private TileDAO(){}

    public static TileDAO getInstance(){
        if (instance == null){
            instance = new TileDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<Tile> tiles = new ArrayList<>();
        try {
            List<DBTile> dbTiles = InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().queryForAll();
            for(DBTile dbTile : dbTiles){
                tiles.add(mapDBTileToTile(dbTile));
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
            tile = mapDBTileToTile(dbTile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tile;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().create(mapTileToDBTile((Tile) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean update(Object object){
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getTileDAO().update(mapTileToDBTile((Tile) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        boolean ok = false;
        // TODO falls n�tig
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

    public Tile mapDBTileToTile(DBTile dbTile) {
        Tile tile = new Tile();

        tile.setTileSize(dbTile.getTileSize());
        tile.setIsActivated(dbTile.getIsActivated());
        tile.setDrawableId(dbTile.getDrawableId());
        tile.setName(dbTile.getName());
        tile.setTileID(dbTile.getTileID());
        tile.setScreen(dbTile.getScreen());
        tile.setSyncStatus(dbTile.isSyncStatus());

        return tile;
    }
    public DBTile mapTileToDBTile(Tile tile) {
        DBTile dbTile = new DBTile();

        dbTile.setTileID(tile.getTileID());
        dbTile.setTileSize(tile.getTileSize());
        dbTile.setIsActivated(tile.getIsActivated());
        dbTile.setDrawableId(tile.getDrawableId());
        dbTile.setName(tile.getName());
        dbTile.setScreen(tile.getScreen());
        dbTile.setSyncStatus(tile.isSyncStatus());

        return dbTile;
    }
}
