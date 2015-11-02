package wipraktikum.informationwallandroidapp.Database.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class BlackBoardItemDAO implements IDAO {
    static BlackBoardItemDAO instance = null;

    private BlackBoardItemDAO(){}

    public static BlackBoardItemDAO getInstance(){
        if (instance == null){
            instance = new BlackBoardItemDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<BlackBoardItem> blackBoardItems = new ArrayList<>();;
        try {
            List<wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem> dbBlackBoardItems = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().queryForAll();
            for(wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem dbBlackBoardItem : dbBlackBoardItems){
                blackBoardItems.add(mapDBBlackBoardItemToBlackBoardItem(dbBlackBoardItem));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackBoardItems;
    }

    @Override
    public Object queryForId(long iD) {
        BlackBoardItem blackBoardItem = null;
        try {
            wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem dbBlackBoardItem = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().queryForId(iD);
            blackBoardItem = mapDBBlackBoardItemToBlackBoardItem(dbBlackBoardItem);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blackBoardItem;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            BlackBoardItem tempItem = (BlackBoardItem) object;
            List<BlackBoardAttachment> tempAttachments = tempItem.getBlackBoardAttachment();

            // Set AttachmentList to null because ORMLite need a empty list as a ForeignCollection
            tempItem.setBlackBoardAttachment(null);
            // Mapping to DB-Object
            DBBlackBoardItem item = (DBBlackBoardItem) this.mapBlackBoardItemToDBBlackBoardItem(tempItem);

            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().create(item);

            // Add  Item reference to Attachment Objects and add it to the db
            for(int i = 0; i < tempAttachments.size(); i++) {
                DBBlackBoardAttachment tempAttachment = DAOHelper.getInstance().getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(tempAttachments.get(i));
                tempAttachment.setBlackBoardItem(item);
                InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().create(tempAttachment);
            }

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
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().update(mapBlackBoardItemToDBBlackBoardItem((BlackBoardItem) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public BlackBoardItem mapDBBlackBoardItemToBlackBoardItem(DBBlackBoardItem dbBlackBoardItem) {
        BlackBoardItem blackBoardItem = new BlackBoardItem();

        blackBoardItem.setBlackBoardItemID(dbBlackBoardItem.getBlackBoardItemID());
        blackBoardItem.setTitle(dbBlackBoardItem.getTitle());
        blackBoardItem.setDescriptionText(dbBlackBoardItem.getDescriptionText());
        blackBoardItem.setCreatedTimestamp(dbBlackBoardItem.getCreatedTimestamp());
        blackBoardItem.setEditedTimestamp(dbBlackBoardItem.getEditedTimestamp());
        blackBoardItem.setContact(DAOHelper.getInstance().
                getContactDAO().mapDBContactToContact(dbBlackBoardItem.getContact()));
        blackBoardItem.setBlackBoardAttachment(DAOHelper.getInstance().
                getBlackBoardAttachmentDAO().mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardItem.getBlackBoardAttachment()));

        return blackBoardItem;
    }

    public DBBlackBoardItem mapBlackBoardItemToDBBlackBoardItem(BlackBoardItem blackBoardItem) {
        wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem dbBlackBoardItem = new wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem();

        dbBlackBoardItem.setBlackBoardItemID(blackBoardItem.getBlackBoardItemID());
        dbBlackBoardItem.setTitle(blackBoardItem.getTitle());
        dbBlackBoardItem.setDescriptionText(blackBoardItem.getDescriptionText());
        dbBlackBoardItem.setCreatedTimestamp(blackBoardItem.getCreatedTimestamp());
        dbBlackBoardItem.setEditedTimestamp(blackBoardItem.getEditedTimestamp());
        dbBlackBoardItem.setContact(DAOHelper.getInstance().
                getContactDAO().mapContactToDBContact(blackBoardItem.getContact()));
        try {
            List<DBBlackBoardAttachment> dbBlackBoardAttachments= DAOHelper.getInstance().
                    getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(blackBoardItem.getBlackBoardAttachment());
            if (dbBlackBoardAttachments != null) {
                dbBlackBoardItem.setBlackBoardAttachment(dbBlackBoardAttachments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbBlackBoardItem;
    }
}
