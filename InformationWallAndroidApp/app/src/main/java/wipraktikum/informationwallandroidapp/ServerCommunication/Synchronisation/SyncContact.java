package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;

/**
 * Created by Eric Schmidt on 28.11.2015.
 */
public class SyncContact implements JsonManager.OnArrayResponseListener{
    private static SyncContact instance = null;
    private JsonManager jsonManagerFromServer = null;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncContact (){
        jsonManagerFromServer = new JsonManager();
        jsonManagerFromServer.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                if (mOnSyncFinishedListener != null){
                    mOnSyncFinishedListener.onSyncFinished();
                }
            }
        });
        jsonManagerFromServer.setOnArrayResponseReceiveListener(this);
    }

    public static SyncContact getInstance(){
        if (instance == null){
            instance = new SyncContact();
        }
        return instance;
    }

    public void syncContacts() {
        syncContactFromServer();
    }

    private void syncContactFromServer(){
        jsonManagerFromServer.getJsonArray(ServerURLManager.GET_ALL_CONTACTS_URL);
    }

    private void UpdateOrCreateContact(JsonElement response) {
        ContactDAO contactDAO = DAOHelper.getContactDAO();

        deleteAllContacts();

        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Contact> serverItemList = gsonInstance.fromJson(response, new TypeToken<List<Contact>>(){}.getType());

        for(Contact serverContact : serverItemList) {
            serverContact.setSyncStatus(true);
            contactDAO.createOrUpdate(serverContact);
        }
    }

    public void deleteAllContacts(){
        ContactDAO contactDAO = DAOHelper.getContactDAO();
        ArrayList<Contact> contacts = contactDAO.queryForAll();

        for (Contact contact : contacts){
            contactDAO.delete(contact);
        }

    }

    @Override
    public void OnResponse(JSONArray response) {
        UpdateOrCreateContact(new JsonParser().parse(response.toString()));
        if (mOnSyncFinishedListener != null){
            mOnSyncFinishedListener.onSyncFinished();
        }
    }

    public void setOnSyncFinishedListener(OnSyncFinishedListener onSyncFinishedListener){
        mOnSyncFinishedListener = onSyncFinishedListener;
    }

    public interface OnSyncFinishedListener{
        void onSyncFinished();
    }
}
