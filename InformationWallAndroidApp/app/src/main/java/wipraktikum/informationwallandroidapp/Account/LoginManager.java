package wipraktikum.informationwallandroidapp.Account;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.sql.SQLException;

import wipraktikum.informationwallandroidapp.BusinessObject.User.User;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Remi on 14.11.2015.
 */
public class LoginManager {

    private OnRequestLoginResponseReceived mOnRequestLoginResponseReceived;

    public void requestLogin(final User loginUser, final boolean keepLoggedIn) {
        JsonManager jsonManager = new JsonManager();
        jsonManager.sendJson(ServerURLManager.LOG_IN_AUTHENTICATION_URL, loginUser);
        jsonManager.setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                if (mOnRequestLoginResponseReceived != null) {
                    mOnRequestLoginResponseReceived.onRequestLoginResponseReceived(true);
                }
                logInUser(createResponseUser(response, loginUser.getServerURL(), keepLoggedIn));
                InfoWallApplication.updateCurrentUser();
            }
        });
        jsonManager.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                if (mOnRequestLoginResponseReceived != null) {
                    mOnRequestLoginResponseReceived.onRequestLoginResponseReceived(false);
                }
            }
        });
    }

    /**
     * Create a User Object using the response json from the server and the serverURL from the GUI
     *
     * @param response
     * @param serverURL
     * @param keepLoggedIn
     * @return
     */
    private User createResponseUser(JSONObject response, String serverURL, boolean keepLoggedIn) {
        User currentUser = new Gson().fromJson(new JsonParser().parse(response.toString()), User.class);
        currentUser.setPreviousLoggedIn(true);
        currentUser.setServerURL(serverURL);
        if (keepLoggedIn) currentUser.setKeepLogInData(keepLoggedIn);
        return currentUser;
    }

    private static void saveUser2DB(User user) {
        DAOHelper.getUserDAO().createOrUpdate(user);
    }

    public static void logInUser(User user){
        user.setLoggedIn(true);
        saveUser2DB(user);
    }

    public static void logOutUser(User user) {
        user.setLoggedIn(false);
        saveUser2DB(user);
    }


    public void setOnRequestLoginResponseReceived(OnRequestLoginResponseReceived onRequestLoginResponseReceived){
        this.mOnRequestLoginResponseReceived = onRequestLoginResponseReceived;
    }

    public interface OnRequestLoginResponseReceived {
        void onRequestLoginResponseReceived(boolean successful);
    }

    public static boolean existPreviousAccountData(){
        try {
            if (!DAOHelper.getUserDAO().getPreviousLoggedInAccounts().isEmpty()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
