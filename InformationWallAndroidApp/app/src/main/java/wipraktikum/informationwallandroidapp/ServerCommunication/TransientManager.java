package wipraktikum.informationwallandroidapp.ServerCommunication;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;

/**
 * Created by Eric Schmidt on 02.12.2015.
 */
public class TransientManager {
    public static User keepTransientUserData(User serverUser) {
        if (serverUser != null) {
            User clientUser = (User) DAOHelper.getUserDAO()
                    .queryForId(serverUser.getUserID());
            if (clientUser != null) {
                serverUser.setServerURL(clientUser.getServerURL());
                serverUser.setKeepLogInData(clientUser.isKeepLogInData());
                serverUser.setPreviousLoggedIn(clientUser.isPreviousLoggedIn());
                serverUser.setLoggedIn(clientUser.isLoggedIn());
            }
        }
        return serverUser;
    }
}
