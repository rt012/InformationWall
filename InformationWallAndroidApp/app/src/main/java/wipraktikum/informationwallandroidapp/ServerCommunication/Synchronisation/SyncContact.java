package wipraktikum.informationwallandroidapp.ServerCommunication.Synchronisation;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.Database.DAO.Contact.ContactDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Eric Schmidt on 28.11.2015.
 */
public class SyncContact implements JsonManager.OnObjectResponseListener, JsonManager.OnArrayResponseListener{
    private static SyncContact instance = null;
    private JsonManager jsonManagerToServer = null;
    private JsonManager jsonManagerFromServer = null;
    private Contact currentUnsyncedContact;

    private OnSyncFinishedListener mOnSyncFinishedListener = null;

    private SyncContact (){
        jsonManagerToServer = new JsonManager();
        jsonManagerToServer.setOnObjectResponseReceiveListener(this);
        jsonManagerToServer.setOnErrorReceiveListener(new JsonManager.OnErrorListener() {
            @Override
            public void OnErrorResponse(VolleyError error) {
                syncContactFromServer();
            }
        });
        jsonManagerToServer.setOnArrayResponseReceiveListener(this);

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
        syncContactToServer();
    }

    private void syncContactToServer(){
        try {
            ArrayList<Contact> unsyncedItems = DAOHelper.getContactDAO().getUnsyncedItems();
            if(!unsyncedItems.isEmpty()) {
                currentUnsyncedContact = unsyncedItems.get(0);

                JSONObject jsonObject = JSONBuilder.createJSONFromParam(ServerURLManager.NEW_CONTACT_KEY,
                        JSONBuilder.createJSONFromObject(currentUnsyncedContact));
                jsonManagerToServer.sendJson(ServerURLManager.UPDATE_BLACKBOARD_BEHAVIOUR_URL, jsonObject);
            } else {
                syncContactFromServer();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void OnResponse(JSONObject response) {
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Contact serverContact = gsonInstance.fromJson(new JsonParser().parse(response.toString()), Contact.class);
        serverContact.setSyncStatus(true);
        ContactDAO contactDAO = DAOHelper.getContactDAO();
        contactDAO.deleteByID(currentUnsyncedContact.getContactID());
        contactDAO.createOrUpdate(serverContact);

        syncContacts();
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
