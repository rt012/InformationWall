package wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.DAO.IDAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class BlackBoardAttachmentDAO implements IDAO {
    private static BlackBoardAttachmentDAO instance = null;

    private BlackBoardAttachmentDAO(){}

    public static BlackBoardAttachmentDAO getInstance(){
        if (instance == null){
            instance = new BlackBoardAttachmentDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<BlackBoardAttachment> blackBoardAttachments = new ArrayList<>();;
        try {
            List<DBBlackBoardAttachment> dbBlackBoardAttachments = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().queryForAll();
            for(DBBlackBoardAttachment dbBlackBoardAttachment : dbBlackBoardAttachments){
                blackBoardAttachments.add(mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardAttachment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackBoardAttachments;
    }

    @Override
    public Object queryForId(long iD) {
        BlackBoardAttachment blackBoardAttachment = null;
        try {
            DBBlackBoardAttachment dbBlackBoardAttachment = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().queryForId(iD);
            blackBoardAttachment = mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardAttachment);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackBoardAttachment;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().create(mapBlackBoardAttachmentToDBBlackBoardAttachment((BlackBoardAttachment) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().update(mapBlackBoardAttachmentToDBBlackBoardAttachment((BlackBoardAttachment) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        return false;
    }

    @Override
    public boolean deleteByID(long Id) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public BlackBoardAttachment mapDBBlackBoardAttachmentToBlackBoardAttachment(DBBlackBoardAttachment dbBlackBoardAttachment) {
        BlackBoardAttachment blackBoardAttachment = new BlackBoardAttachment();

        if (dbBlackBoardAttachment != null) {
            blackBoardAttachment.setBlackBoardAttachmentID(dbBlackBoardAttachment.getBlackBoardAttachmentID());
            blackBoardAttachment.setName(dbBlackBoardAttachment.getName());
            if (dbBlackBoardAttachment.getDataType() != null) {
                blackBoardAttachment.setDataType(dbBlackBoardAttachment.getDataType());
            }else{
                blackBoardAttachment.setDataType(DBBlackBoardAttachment.DataType.OTHER);
            }
            blackBoardAttachment.setDeviceDataPath(dbBlackBoardAttachment.getDeviceDataPath());
            blackBoardAttachment.setRemoteDataPath(dbBlackBoardAttachment.getRemoteDataPath());
            blackBoardAttachment.setBlackBoardItem(dbBlackBoardAttachment.getBlackBoardItem());
            blackBoardAttachment.setSyncStatus(dbBlackBoardAttachment.isSyncStatus());
        }
        return blackBoardAttachment;
    }

    public DBBlackBoardAttachment mapBlackBoardAttachmentToDBBlackBoardAttachment(BlackBoardAttachment blackBoardAttachment) {
        DBBlackBoardAttachment dbBlackBoardAttachment = new DBBlackBoardAttachment();

        if (blackBoardAttachment != null) {
            dbBlackBoardAttachment.setBlackBoardAttachmentID(blackBoardAttachment.getBlackBoardAttachmentID());
            dbBlackBoardAttachment.setName(blackBoardAttachment.getName());
            dbBlackBoardAttachment.setDataType(blackBoardAttachment.getDataType());
            dbBlackBoardAttachment.setDeviceDataPath(blackBoardAttachment.getDeviceDataPath());
            dbBlackBoardAttachment.setRemoteDataPath(blackBoardAttachment.getRemoteDataPath());
            dbBlackBoardAttachment.setBlackBoardItem(blackBoardAttachment.getBlackBoardItem());
            dbBlackBoardAttachment.setSyncStatus(blackBoardAttachment.isSyncStatus());
        }
        return dbBlackBoardAttachment;
    }

    public List mapDBBlackBoardAttachmentToBlackBoardAttachment(List<DBBlackBoardAttachment> dbBlackBoardAttachments) {
        if (dbBlackBoardAttachments == null) return null;

        ArrayList<BlackBoardAttachment> blackBoardAttachments = new ArrayList();

        for(DBBlackBoardAttachment dbBlackBoardAttachment : dbBlackBoardAttachments){
            blackBoardAttachments.add(mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardAttachment));
        }

        return blackBoardAttachments;
    }

    public List<DBBlackBoardAttachment> mapBlackBoardAttachmentToDBBlackBoardAttachment(List<BlackBoardAttachment>  blackBoardAttachments) {
        if (blackBoardAttachments == null) return null;

        ArrayList<DBBlackBoardAttachment> dbBlackBoardAttachments = new ArrayList<DBBlackBoardAttachment>();

        for(BlackBoardAttachment blackBoardAttachment : blackBoardAttachments){
            dbBlackBoardAttachments.add(mapBlackBoardAttachmentToDBBlackBoardAttachment(blackBoardAttachment));
        }

        return dbBlackBoardAttachments;
    }
}
