package wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class BlackBoardItemDAO implements DAO {
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
            DBBlackBoardItem dbBlackBoardItem = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().queryForId(iD);
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
            DBBlackBoardItem blackBoardItem = (DBBlackBoardItem) this.mapBlackBoardItemToDBBlackBoardItem(tempItem);
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().create(blackBoardItem);

            // Add  Item reference to Attachment Objects and add it to the db
            for(int i = 0; i < tempAttachments.size(); i++) {
                DBBlackBoardAttachment tempAttachment = DAOHelper.getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(tempAttachments.get(i));
                tempAttachment.setBlackBoardItem(blackBoardItem);
                //InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().create(tempAttachment);
                InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().createOrUpdate(tempAttachment);
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
            BlackBoardItem blackBoardItem = (BlackBoardItem) object;

            //Manage the foreigner objects
            DAOHelper.getUserGroupDAO().createOrUpdate(blackBoardItem.getUser());
            DAOHelper.getContactDAO().createOrUpdate(blackBoardItem.getContact());

            List<BlackBoardAttachment> tempAttachments = blackBoardItem.getBlackBoardAttachment();
            // Set AttachmentList to null because ORMLite need a empty list as a ForeignCollection
            blackBoardItem.setBlackBoardAttachment(null);
            // Mapping to DB-Object
            DBBlackBoardItem dbBlackBoardItem = (DBBlackBoardItem) this.mapBlackBoardItemToDBBlackBoardItem(blackBoardItem);

            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().createOrUpdate(dbBlackBoardItem);

            // Add  Item reference to Attachment Objects and add it to the db
            for(int i = 0; i < tempAttachments.size(); i++) {
                DBBlackBoardAttachment tempAttachment = DAOHelper.getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(tempAttachments.get(i));
                tempAttachment.setBlackBoardItem(dbBlackBoardItem);
                //InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().update(tempAttachment);
                InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().createOrUpdate(tempAttachment);
            }

            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public boolean createOrUpdate(Object object){
        boolean ok = false;
        try {
            BlackBoardItem blackBoardItem = (BlackBoardItem) object;

            //Manage the foreigner objects
            DAOHelper.getUserDAO().createOrUpdate(blackBoardItem.getUser());
            DAOHelper.getContactDAO().createOrUpdate(blackBoardItem.getContact());

            List<BlackBoardAttachment> tempAttachments = blackBoardItem.getBlackBoardAttachment();
            // Set AttachmentList to null because ORMLite need a empty list as a ForeignCollection
            blackBoardItem.setBlackBoardAttachment(null);
            // Mapping to DB-Object
            DBBlackBoardItem dbBlackBoardItem = (DBBlackBoardItem) this.mapBlackBoardItemToDBBlackBoardItem(blackBoardItem);

            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().createOrUpdate(dbBlackBoardItem);

            // Add  Item reference to Attachment Objects and add it to the db
            for(int i = 0; i < tempAttachments.size(); i++) {
                DBBlackBoardAttachment tempAttachment = DAOHelper.getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(tempAttachments.get(i));
                tempAttachment.setBlackBoardItem(dbBlackBoardItem);
                InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().createOrUpdate(tempAttachment);
            }
            // Set Attachments to the item again because in the create method we have to clean erase this reference ( because of ORMLite )
            blackBoardItem.setBlackBoardAttachment(tempAttachments);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean delete(Object object) {
        boolean ok = false;
        try {
            BlackBoardItem blackBoardItem = (BlackBoardItem) object;
            //To get the database object and not the edited one
            blackBoardItem = (BlackBoardItem) queryForId(blackBoardItem.getBlackBoardItemID());

            if(blackBoardItem.getBlackBoardAttachment() != null && !blackBoardItem.getBlackBoardAttachment().isEmpty()) {
                for(BlackBoardAttachment blackBoardAttachment : blackBoardItem.getBlackBoardAttachment()) {
                    InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardAttachmentDAO().deleteById(blackBoardAttachment.getBlackBoardAttachmentID());
                }
            }
            blackBoardItem.setUser(null);
            blackBoardItem.setContact(null);
            //blackBoardItem.setBlackBoardAttachment(null);
            InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO().deleteById(blackBoardItem.getBlackBoardItemID());
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
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

    public ArrayList<BlackBoardItem> getUnsyncedItems() throws SQLException{
        Dao<DBBlackBoardItem, Long> blackBoardItemDAO = InfoWallApplication.getInstance().getDatabaseHelper().getBlackBoardItemDAO();
        // get our query builder from the DAO
        QueryBuilder<DBBlackBoardItem, Long> queryBuilder =
                blackBoardItemDAO.queryBuilder();
        queryBuilder.where().eq(DBBlackBoardItem.SYNCSTATUS_FIELD_NAME, false);
        PreparedQuery<DBBlackBoardItem> preparedQuery = queryBuilder.prepare();
        List<DBBlackBoardItem> dbBlackBoardItems = blackBoardItemDAO.query(preparedQuery);

        ArrayList<BlackBoardItem> unsycedBlackBoardItem = new ArrayList<BlackBoardItem>();
        if(dbBlackBoardItems != null || !dbBlackBoardItems.isEmpty()) {
            for(DBBlackBoardItem dbBlackBoardItem : dbBlackBoardItems) {
                unsycedBlackBoardItem.add(mapDBBlackBoardItemToBlackBoardItem(dbBlackBoardItem));
            }
        }
       return unsycedBlackBoardItem;
    }

    public BlackBoardItem mapDBBlackBoardItemToBlackBoardItem(DBBlackBoardItem dbBlackBoardItem) {
        BlackBoardItem blackBoardItem = new BlackBoardItem();

        blackBoardItem.setBlackBoardItemID(dbBlackBoardItem.getBlackBoardItemID());
        blackBoardItem.setTitle(dbBlackBoardItem.getTitle());
        blackBoardItem.setDescriptionText(dbBlackBoardItem.getDescriptionText());
        blackBoardItem.setLayoutType(dbBlackBoardItem.getLayoutType());
        blackBoardItem.setCreatedTimestamp(dbBlackBoardItem.getCreatedTimestamp());
        blackBoardItem.setEditedTimestamp(dbBlackBoardItem.getEditedTimestamp());
        blackBoardItem.setContact(DAOHelper.
                getContactDAO().mapDBContactToContact(dbBlackBoardItem.getContact()));
        blackBoardItem.setBlackBoardAttachment(DAOHelper.
                getBlackBoardAttachmentDAO().mapDBBlackBoardAttachmentToBlackBoardAttachment(dbBlackBoardItem.getBlackBoardAttachment()));
        blackBoardItem.setSyncStatus(dbBlackBoardItem.isSyncStatus());
        blackBoardItem.setUser(DAOHelper.getUserDAO().mapDBUserToUser(dbBlackBoardItem.getUser()));
        return blackBoardItem;
    }

    public DBBlackBoardItem mapBlackBoardItemToDBBlackBoardItem(BlackBoardItem blackBoardItem) {
        wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem dbBlackBoardItem = new wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardItem();

        dbBlackBoardItem.setBlackBoardItemID(blackBoardItem.getBlackBoardItemID());
        dbBlackBoardItem.setTitle(blackBoardItem.getTitle());
        dbBlackBoardItem.setDescriptionText(blackBoardItem.getDescriptionText());
        dbBlackBoardItem.setLayoutType(blackBoardItem.getLayoutType());
        dbBlackBoardItem.setCreatedTimestamp(blackBoardItem.getCreatedTimestamp());
        dbBlackBoardItem.setEditedTimestamp(blackBoardItem.getEditedTimestamp());
        dbBlackBoardItem.setContact(DAOHelper.
                getContactDAO().mapContactToDBContact(blackBoardItem.getContact()));
        try {
            List<DBBlackBoardAttachment> dbBlackBoardAttachments= DAOHelper.
                    getBlackBoardAttachmentDAO().mapBlackBoardAttachmentToDBBlackBoardAttachment(blackBoardItem.getBlackBoardAttachment());
            if (dbBlackBoardAttachments != null) {
                dbBlackBoardItem.setBlackBoardAttachment(dbBlackBoardAttachments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbBlackBoardItem.setSyncStatus(blackBoardItem.isSyncStatus());
        dbBlackBoardItem.setUser(DAOHelper.getUserDAO().mapUserToDBUser(blackBoardItem.getUser()));

        return dbBlackBoardItem;
    }
}
