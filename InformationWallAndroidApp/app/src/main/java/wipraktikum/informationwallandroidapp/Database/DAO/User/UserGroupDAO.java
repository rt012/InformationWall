package wipraktikum.informationwallandroidapp.Database.DAO.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.User.UserGroup;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.User.DBUserGroup;
import wipraktikum.informationwallandroidapp.Database.DAO.IDAO;
import wipraktikum.informationwallandroidapp.InfoWallApplication;

/**
 * Created by Eric Schmidt on 07.11.2015.
 */
public class UserGroupDAO implements IDAO {
    private static UserGroupDAO instance = null;

    private UserGroupDAO(){}

    public static UserGroupDAO getInstance(){
        if (instance == null){
            instance = new UserGroupDAO();
        }
        return instance;
    }

    @Override
    public ArrayList queryForAll() {
        ArrayList<UserGroup> userGroups = new ArrayList<>();
        try {
            List<DBUserGroup> dbUserGroups = InfoWallApplication.getInstance().getDatabaseHelper().getUserGroupDAO().queryForAll();
            for(DBUserGroup dbUserGroup : dbUserGroups){
                userGroups.add(mapDBUserGroupToUserGroup(dbUserGroup));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userGroups;
    }

    @Override
    public Object queryForId(long iD) {
        UserGroup userGroup = null;
        try {
            DBUserGroup dbUserGroup = InfoWallApplication.getInstance().getDatabaseHelper().getUserGroupDAO().queryForId(iD);
            userGroup = mapDBUserGroupToUserGroup(dbUserGroup);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userGroup;
    }

    @Override
    public boolean create(Object object) {
        boolean ok = false;
        try {
            InfoWallApplication.getInstance().getDatabaseHelper().getUserGroupDAO().create(mapUserGroupToDBUserGroup((UserGroup) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getUserGroupDAO().update(mapUserGroupToDBUserGroup((UserGroup) object));
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
            InfoWallApplication.getInstance().getDatabaseHelper().getUserGroupDAO().deleteById(Id);
            ok = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }

    public UserGroup mapDBUserGroupToUserGroup(DBUserGroup dbUserGroup) {
        UserGroup userGroup = new UserGroup();
        if(dbUserGroup != null) {
            userGroup.setUserGroupID(dbUserGroup.getUserGroupID());
            userGroup.setUserGroupName(dbUserGroup.getUserGroupName());
            userGroup.setDelete(dbUserGroup.canDelete());
            userGroup.setEdit(dbUserGroup.canEdit());
            userGroup.setWrite(dbUserGroup.canWrite());
            userGroup.setRead(dbUserGroup.canRead());
        }
        return userGroup;
    }
    public DBUserGroup mapUserGroupToDBUserGroup(UserGroup userGroup) {
        DBUserGroup dbUserGroup = new DBUserGroup();
        if(userGroup != null) {
            dbUserGroup.setUserGroupID(userGroup.getUserGroupID());
            dbUserGroup.setUserGroupName(userGroup.getUserGroupName());
            dbUserGroup.setDelete(userGroup.canDelete());
            dbUserGroup.setEdit(userGroup.canEdit());
            dbUserGroup.setWrite(userGroup.canWrite());
            dbUserGroup.setRead(userGroup.canRead());
        }
        return dbUserGroup;
    }
}
