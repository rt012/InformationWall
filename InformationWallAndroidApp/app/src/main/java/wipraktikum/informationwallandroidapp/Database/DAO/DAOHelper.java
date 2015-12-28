package wipraktikum.informationwallandroidapp.Database.DAO;

import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardAttachmentDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactAddressDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.FeedReader.FeedReaderDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Tile.TileDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.User.UserDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.User.UserGroupDAO;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class DAOHelper {
    public static TileDAO getTileDAO(){
        return TileDAO.getInstance();
    }

    public static ContactDAO getContactDAO(){
        return ContactDAO.getInstance();
    }

    public static ContactAddressDAO getContactAddressDAO(){
        return ContactAddressDAO.getInstance();
    }

    public static BlackBoardItemDAO getBlackBoardItemDAO(){
        return BlackBoardItemDAO.getInstance();
    }

    public static BlackBoardAttachmentDAO getBlackBoardAttachmentDAO(){
        return BlackBoardAttachmentDAO.getInstance();
    }

    public static UserDAO getUserDAO() {return UserDAO.getInstance();}

    public static UserGroupDAO getUserGroupDAO() {return UserGroupDAO.getInstance();}

    public static FeedReaderDAO getFeedReaderDAO(){
        return FeedReaderDAO.getInstance();
    }

}
