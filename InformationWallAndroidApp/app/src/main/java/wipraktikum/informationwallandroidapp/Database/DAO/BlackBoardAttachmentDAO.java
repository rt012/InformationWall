package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
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

        blackBoardAttachment.setBlackBoardAttachmentID(dbBlackBoardAttachment.getBlackBoardAttachmentID());
        blackBoardAttachment.setDataType(dbBlackBoardAttachment.getDataType());
        blackBoardAttachment.setDeviceDataPath(dbBlackBoardAttachment.getDeviceDataPath());
        blackBoardAttachment.setRemoteDataPath(dbBlackBoardAttachment.getRemoteDataPath());

        return blackBoardAttachment;
    }

    public DBBlackBoardAttachment mapBlackBoardAttachmentToDBBlackBoardAttachment(BlackBoardAttachment blackBoardAttachment) {
        DBBlackBoardAttachment dbBlackBoardAttachment = new DBBlackBoardAttachment();

        dbBlackBoardAttachment.setBlackBoardAttachmentID(blackBoardAttachment.getBlackBoardAttachmentID());
        dbBlackBoardAttachment.setDataType(blackBoardAttachment.getDataType());
        dbBlackBoardAttachment.setDeviceDataPath(blackBoardAttachment.getDeviceDataPath());
        dbBlackBoardAttachment.setRemoteDataPath(blackBoardAttachment.getRemoteDataPath());

        return dbBlackBoardAttachment;
    }

    public List mapDBBlackBoardAttachmentToBlackBoardAttachment(List<DBBlackBoardAttachment> dbBlackBoardAttachments) {
        ArrayList<BlackBoardAttachment> blackBoardAttachments = new ArrayList();

        for(DBBlackBoardAttachment dbBlackBoardAttachment : dbBlackBoardAttachments){
            blackBoardAttachments.add(mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardAttachment));
        }

        return blackBoardAttachments;
    }

    public List mapBlackBoardAttachmentToDBBlackBoardAttachment(List<BlackBoardAttachment>  blackBoardAttachments) {
        ArrayList<DBBlackBoardAttachment> dbBlackBoardAttachments = new ArrayList();

        for(BlackBoardAttachment blackBoardAttachment : blackBoardAttachments){
            dbBlackBoardAttachments.add(mapBlackBoardAttachmentToDBBlackBoardAttachment(blackBoardAttachment));
        }

        return dbBlackBoardAttachments;
    }
}
