package wipraktikum.informationwallandroidapp.Database.DAO;

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
}
