package wipraktikum.informationwallandroidapp.Database.DAO.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.User.DBUser;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Database.DAO.DAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class UserDAO implements DAO {
    private static UserDAO instance = null;

    private UserDAO(){}

    public static UserDAO getInstance(){
        if (instance == null){
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<User> users = new ArrayList<>();
        try {
            List<DBUser> dbUsers = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO().queryForAll();
            for(DBUser dbUser : dbUsers){
                users.add(mapDBUserToUser(dbUser));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Object queryForId(long iD) {
        User user = null;
        try {
            DBUser dbUser = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO().queryForId(iD);
            user = mapDBUserToUser(dbUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            User user = (User) object;
            user.setUserID(0);
            InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO().create(mapUserToDBUser(user));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean update(Object object) {
        boolean ok = false;
        try {
            Dao<DBUser, Long> userDAO = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO();
            DAOHelper.getUserGroupDAO().update(((User) object).getUserGroup());
            userDAO.update(mapUserToDBUser((User) object));
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public boolean createOrUpdate(Object object) {
        boolean ok = false;
        try {
            Dao<DBUser, Long> userDAO = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO();
            User user = (User) object;
            DAOHelper.getUserGroupDAO().createOrUpdate(user.getUserGroup());
            userDAO.createOrUpdate(mapUserToDBUser(user));

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
            InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public ArrayList<User> getPreviousLoggedInAccounts() throws SQLException{
        Dao<DBUser, Long> userDAO = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO();
        // get our query builder from the DAO
        QueryBuilder<DBUser, Long> queryBuilder =
                userDAO.queryBuilder();
        queryBuilder.where().eq(DBUser.PREVIOUS_LOGGED_IN_FIELD_NAME, true);
        PreparedQuery<DBUser> preparedQuery = queryBuilder.prepare();
        List<DBUser> DBUsers = userDAO.query(preparedQuery);

        ArrayList<User> previousLoggedInUsers = new ArrayList<User>();
        if(DBUsers != null || !DBUsers.isEmpty()) {
            for(DBUser dbUser : DBUsers) {
                previousLoggedInUsers.add(mapDBUserToUser(dbUser));
            }
        }
        return previousLoggedInUsers;
    }

    public List<String> getAllServerURL() throws SQLException{
        Dao<DBUser, Long> userDAO = InfoWallApplication.getInstance().getDatabaseHelper().getUserDAO();
        List<String> serverURLList = new ArrayList<>();

        List<DBUser> results = userDAO.queryBuilder()
                .distinct().selectColumns(DBUser.SERVER_URL_FIELD_NAME).query();
        for(DBUser dbUser : results){
            serverURLList.add(dbUser.getServerURL());
        }

        return serverURLList;
    }

    public User mapDBUserToUser(DBUser dbUser) {
        User user = new User();
        if(dbUser != null) {
            user.setUserID(dbUser.getUserID());
            user.setPassword(dbUser.getPassword());
            user.setEmailAddress(dbUser.getEmailAddress());
            user.setUserGroup(DAOHelper.getUserGroupDAO().
                    mapDBUserGroupToUserGroup(dbUser.getUserGroup()));
            user.setServerURL(dbUser.getServerURL());
            user.setLoggedIn(dbUser.isLoggedIn());
            user.setPreviousLoggedIn(dbUser.isPreviousLoggedIn());
            user.setKeepLogInData(dbUser.isKeepLogInData());
        }
        return user;
    }
    public DBUser mapUserToDBUser(User user) {
        DBUser dbUser = new DBUser();
        if(user != null) {
            dbUser.setUserID(user.getUserID());
            dbUser.setPassword(user.getPassword());
            dbUser.setEmailAddress(user.getEmailAddress());
            dbUser.setUserGroup(DAOHelper.getUserGroupDAO().
                    mapUserGroupToDBUserGroup(user.getUserGroup()));
            dbUser.setServerURL(user.getServerURL());
            dbUser.setLoggedIn(user.isLoggedIn());
            dbUser.setPreviousLoggedIn(user.isPreviousLoggedIn());
            dbUser.setKeepLogInData(user.isKeepLogInData());
        }
        return dbUser;
    }

    public User getCurrentUser() {
        List<User> userList = queryForAll();
        for(User user : userList) {
            if(user.isLoggedIn()) return user;
        }
        return null;
    }
}
