package wipraktikum.informationwallandroidapp.Database.DAO;

import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardAttachmentDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactAddressDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.Tile.TileDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.User.UserDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.User.UserGroupDAO;

/**
 * Created by Eric Schmidt on 28.10.2015.
 */
public class DAOHelper {
    private static DAOHelper instance = null;

    private DAOHelper(){}

    public static DAOHelper getInstance(){
        if(instance == null){
            instance = new DAOHelper();
        }
        return instance;
    }

    public TileDAO getTileDAO(){
        return TileDAO.getInstance();
    }

    public ContactDAO getContactDAO(){
        return ContactDAO.getInstance();
    }

    public ContactAddressDAO getContactAddressDAO(){
        return ContactAddressDAO.getInstance();
    }

    public BlackBoardItemDAO getBlackBoardItemDAO(){
        return BlackBoardItemDAO.getInstance();
    }

    public BlackBoardAttachmentDAO getBlackBoardAttachmentDAO(){
        return BlackBoardAttachmentDAO.getInstance();
    }

    public UserDAO getUserDAO() {return UserDAO.getInstance();}

    public UserGroupDAO getUserGroupDAO() {return UserGroupDAO.getInstance();}
}
