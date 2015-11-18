package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardAutoCompleteTextViewContactAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.CustomView.BlackBoardAttachmentView;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.DAO.BlackBoard.BlackBoardItemDAO;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.PhpRequestManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.UploadManager;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.RealPathHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment implements BlackBoard.OnActivityResultListener, TextWatcher,
        JsonManager.OnObjectResponseListener, JsonManager.OnErrorListener {
    public static final String BLACK_BOARD_ITEM_ID_TAG = "blackBoardItemID";
    private static final String BLACK_BOARD_SAVED_INSTANCE_TAG = "blackBoardJSON";


    private OnSaveBlackBoardItemListener mOnSaveBlackBoardItemListener = null;
    private OnStartActivityResultListener mOnStartActivityResultListener = null;

    private static BlackBoardAddItem instance = null;

    private BlackBoardItem blackBoardItem = null;
    private Contact selectedContact = null;
    private BlackBoardAutoCompleteTextViewContactAdapter autoCompleteTextViewContactAdapter = null;
    private List<BlackBoardAttachment> blackBoardAttachments = new ArrayList<>();
    private List<BlackBoardAttachment> blackBoardAttachmentsCopy = new ArrayList<>();
    private ArrayList<View> blackBoardAttachmentViews = new ArrayList<>();
    private ArrayList<View> blackBoardAttachmentViewsCopy = new ArrayList<>();
    private boolean isEditedItem = false;

    private TableLayout tlAddContact = null;
    private AutoCompleteTextView autoCompleteTextViewContact = null;
    private EditText editTextFullName = null;
    private EditText editTextEmail= null;
    private EditText editTextTelephone = null;
    private EditText editTextCompany = null;
    private EditText editTextStreet = null;
    private EditText editTextCity = null;
    private EditText editTextTitle = null;
    private EditText editTextDescription = null;
    private ImageButton imageButtonExpandContact = null;
    private LinearLayout attachmentContainer = null;
    private Button buttonAttachment = null;

    public static BlackBoardAddItem getInstance(){
        if (instance==null){
            instance = new BlackBoardAddItem();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_add_item, viewGroup, false);
        setHasOptionsMenu(true);

        ((BlackBoard)getActivity()).setOnActivityResultListener(this);

        setUpGUI(view);

        //Write BlackBoardItem Information to View
        if (getArguments() != null){
            blackBoardItem = (BlackBoardItem) DAOHelper.getInstance().getBlackBoardItemDAO().queryForId(
                    getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
            isEditedItem = true;
            setBlackBoardItemInformation();
        }else{
            blackBoardItem = new BlackBoardItem();
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        //Tell Server to open Live Preview
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_SHOW);
        PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_URL, params);

        //Handle initial Live Preview
        sendLivePreviewToServer();
    }

    @Override
    public void onPause() {
        super.onPause();

        removeErrorsFromTextFields();

        //Tell Server to close Live Preview
        Map<String, String> params = new HashMap<>();
        params.put(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_KEY, ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_HIDE);

        PhpRequestManager.getInstance().phpRequest(ServerURLManager.SHOW_LIVE_PREVIEW_BLACK_BOARD_ITEM_URL, params);
    }

    private void removeErrorsFromTextFields() {
        editTextFullName.setError(null);
        editTextEmail.setError(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_black_board_add_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_black_board_item_save) {
            if(validateInputs()) {
                saveBlackBoardItem(fillBlackBoardItem());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateInputs() {
        boolean valid = true;

        if(tlAddContact.getVisibility() != View.GONE){
            String name = editTextFullName.getText().toString();
            String email = editTextEmail.getText().toString();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("enter a valid email address");
                valid = false;
            } else {
                editTextEmail.setError(null);
            }

            if(name.isEmpty()) {
                editTextFullName.setError("enter a name");
                valid = false;
            } else {
                editTextFullName.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onActivityResult(Intent data) {
        String filePath = RealPathHelper.getInstance().getRealPathFromURI(data.getData());
        //Add filePath to LinearLayout below
        BlackBoardAttachment blackBoardAttachment = createNewAttachment(filePath);
        View attachmentView = addAttachmentToView(blackBoardAttachment);
        blackBoardAttachments.add(blackBoardAttachment);
        blackBoardAttachmentViews.add(attachmentView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        sendLivePreviewToServer();
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setUpGUI(View view){
        editTextTitle = (EditText) view.findViewById(R.id.edit_black_board_add_item_title);
        editTextTitle.addTextChangedListener(this);

        editTextDescription = (EditText) view.findViewById(R.id.edit_black_board_add_item_description);
        editTextDescription.addTextChangedListener(this);

        autoCompleteTextViewContactAdapter = new BlackBoardAutoCompleteTextViewContactAdapter(
                getActivity(), 0, DAOHelper.getInstance().getContactDAO().queryForAll());
        autoCompleteTextViewContact = (AutoCompleteTextView) view.findViewById(R.id.ac_tv_black_board_add_item_contact);
        autoCompleteTextViewContact.setAdapter(autoCompleteTextViewContactAdapter);
        autoCompleteTextViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = (Contact) autoCompleteTextViewContactAdapter.getItem(position);
                sendLivePreviewToServer();
            }
        });

        tlAddContact = (TableLayout) view.findViewById(R.id.tl_black_board_add_item_contact);
        tlAddContact.setVisibility(View.GONE);

        editTextFullName = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_full_name);
        editTextFullName.addTextChangedListener(this);

        editTextEmail= (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_email);
        editTextEmail.addTextChangedListener(this);

        editTextTelephone = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_telephone);
        editTextTelephone.addTextChangedListener(this);

        editTextCompany = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_company);
        editTextCompany.addTextChangedListener(this);

        editTextStreet = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_street);
        editTextStreet.addTextChangedListener(this);

        editTextCity = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_city);
        editTextCity.addTextChangedListener(this);

        imageButtonExpandContact = (ImageButton) view.findViewById(R.id.ib_tv_black_board_add_item_contact);
        imageButtonExpandContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tlAddContact.getVisibility() == View.GONE) {
                    tlAddContact.setVisibility(View.VISIBLE);
                    autoCompleteTextViewContact.setEnabled(false);
                    autoCompleteTextViewContact.setText(getActivity().getString(R.string.black_board_add_item_new_contact));
                    imageButtonExpandContact.setImageDrawable(getActivity().getDrawable(R.drawable.ic_arrow_up));
                } else {
                    tlAddContact.setVisibility(View.GONE);
                    autoCompleteTextViewContact.setEnabled(true);
                    autoCompleteTextViewContact.setText("");
                    autoCompleteTextViewContact.setHint(getActivity().getString(R.string.black_board_add_item_contact));
                    imageButtonExpandContact.setImageDrawable(getActivity().getDrawable(R.drawable.ic_arrow_down));
                }
            }
        });

        buttonAttachment = (Button) view.findViewById(R.id.b_black_board_add_item_attachment);
        buttonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStartActivityResultListener != null) {
                    mOnStartActivityResultListener.onStartActivityResultListener();
                }

                FileHelper.getInstance().showFileChooser(getActivity());
            }
        });

        attachmentContainer = (LinearLayout) view.findViewById(R.id.ll_attachment_container);
    }

    public void setBlackBoardItemInformation() {
        String title = getBlackBoardItem().getTitle();
        String descriptionText = getBlackBoardItem().getDescriptionText();
        Contact contact = getBlackBoardItem().getContact();
        List<BlackBoardAttachment> blackBoardAttachments = getBlackBoardItem().getBlackBoardAttachment();

        editTextTitle.setText(title);
        editTextDescription.setText(descriptionText);
        if (contact != null) {
            autoCompleteTextViewContact.setText(contact.getFullName());
            selectedContact = contact;
        }

        attachmentContainer.removeAllViews();
        if (blackBoardAttachments != null) {
            for (BlackBoardAttachment blackBoardAttachment : blackBoardAttachments) {
                addAttachmentToView(blackBoardAttachment);
            }
        }
    }

    private void sendLivePreviewToServer(){
        if (blackBoardItem != null) {
            new JsonManager().sendJson(ServerURLManager.LIVE_PREVIEW_BLACK_BOARD_ITEM_JSON_URL, fillBlackBoardItem());
        }
    }

    private BlackBoardItem getBlackBoardItem(){
        return blackBoardItem;
    }

    private BlackBoardItem fillBlackBoardItem(){
        Contact newContact;

        blackBoardItem.setCreatedTimestamp(Calendar.getInstance().getTime());
        blackBoardItem.setEditedTimestamp(Calendar.getInstance().getTime());
        blackBoardItem.setTitle(editTextTitle.getText().toString());
        blackBoardItem.setDescriptionText(editTextDescription.getText().toString());
        blackBoardItem.setBlackBoardAttachment(blackBoardAttachments);
        blackBoardItem.setUser(InfoWallApplication.getCurrentUser());

        //Check if a new contact was created (LinearLayout.View(Visible)) or a existing was selected
        if (tlAddContact.getVisibility() == View.VISIBLE) {
            newContact = createNewContact();
        } else {
            if (selectedContact == null) {
                selectedContact = new Contact();
                selectedContact.setContactAddress(new ContactAddress());
            }
            newContact = selectedContact;
        }
        blackBoardItem.setContact(newContact);

        return blackBoardItem;
    }

    private View addAttachmentToView(BlackBoardAttachment attachment){
        BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(getActivity(), attachment, false);
        attachmentContainer.addView(attachmentView);

        return attachmentView;
    }

    private void saveBlackBoardItem(final BlackBoardItem blackBoardItem){
        if (!isEditTextEmpty(editTextTitle)) {
            blackBoardAttachmentsCopy.addAll(blackBoardAttachments);
            blackBoardAttachmentViewsCopy.addAll(blackBoardAttachmentViews);

            uploadAttachment();
        }else{
            Snackbar
                .make(getView(),  R.string.black_board_add_item_snackbar_no_title, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    //Needs a better name
    private void uploadAttachment(){
        if (!blackBoardAttachmentsCopy.isEmpty()) {
            UploadManager uploadManager = new UploadManager();
            final BlackBoardAttachment blackBoardAttachment = blackBoardAttachmentsCopy.get(0);
            final BlackBoardAttachmentView attachmentView = (BlackBoardAttachmentView) blackBoardAttachmentViewsCopy.get(0);

            //Show Upload Progress
            attachmentView.showProgressbar(true);
            //Upload File
            File attachmentFile = new File(blackBoardAttachment.getDeviceDataPath());
            uploadManager.uploadFile(attachmentFile, ServerURLManager.UPLOAD_BLACK_BOARD_ATTACHMENT_URL);
            uploadManager.setOnUploadFinishedListener(new UploadManager.OnUploadFinishedListener() {
                @Override
                public void onUploadFinished(String remoteDataPath) {
                    //Show Upload has finished
                    attachmentView.showProgressbar(false);
                    //Save remoteDataPath to attachment
                    blackBoardAttachment.setRemoteDataPath(remoteDataPath);

                    blackBoardAttachmentsCopy.remove(0);
                    blackBoardAttachmentViewsCopy.remove(0);

                    uploadAttachment();
                }
            });
        }else{
            saveBlackBoardItem2DB();
        }
    }

    private void saveBlackBoardItem2DB(){
        //Check if item is a edit
        if (isEditedItem){
            blackBoardItem.setSyncStatus(false);
            blackBoardItem.setBlackBoardItemID(getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
        }
        blackBoardAttachments = blackBoardItem.getBlackBoardAttachment();
        DAOHelper.getInstance().getBlackBoardItemDAO().createOrUpdate(blackBoardItem);

        // Set Attachments to the item again because in the create method we have to clean erase this reference ( because of ORMLite )
        blackBoardItem.setBlackBoardAttachment(blackBoardAttachments);

        JsonManager jsonManager = new JsonManager();
        jsonManager.setOnObjectResponseReceiveListener(this);
        jsonManager.setOnErrorReceiveListener(this);
        //jsonManager.sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, blackBoardItem);
    }

    private Contact createNewContact(){
        Contact newContact = new Contact();

        if (!isEditTextEmpty(editTextFullName)) {
            String[] splitFullName = editTextFullName.getText().toString().split(" ");
            if (splitFullName.length == 2) {
                newContact.setSurname(splitFullName[0]);
                newContact.setName(splitFullName[1]);
            } else {
                newContact.setSurname(splitFullName[0]);
            }
        }

        if (!isEditTextEmpty(editTextFullName)) newContact.setTelephone(editTextTelephone.getText().toString());
        if (!isEditTextEmpty(editTextEmail))    newContact.setEMailAddress(editTextEmail.getText().toString());
        if (!isEditTextEmpty(editTextCompany))  newContact.setCompany(editTextCompany.getText().toString());

        ContactAddress newContactAddress = new ContactAddress();

        if (!isEditTextEmpty(editTextStreet)) {
            String[] splitStreet = editTextStreet.getText().toString().split(" ");
            if (splitStreet.length == 2) {
                newContactAddress.setStreetName(splitStreet[0]);
                newContactAddress.setHouseNumber(splitStreet[1]);
            } else {
                newContactAddress.setStreetName(splitStreet[0]);
            }
        }

        if (!isEditTextEmpty(editTextCity)) {
            String[] splitCity = editTextCity.getText().toString().split(" ");
            if (splitCity.length == 2) {
                newContactAddress.setZipCode(splitCity[0]);
                newContactAddress.setCity(splitCity[1]);
            } else {
                newContactAddress.setCity(splitCity[0]);
            }
        }

        newContact.setContactAddress(newContactAddress);

        return newContact;
    }

    private BlackBoardAttachment createNewAttachment(String filePath){
        BlackBoardAttachment blackBoardAttachment = new BlackBoardAttachment();
        blackBoardAttachment.setName(FileHelper.getInstance().getFileName(filePath));
        blackBoardAttachment.setDeviceDataPath(filePath);
        blackBoardAttachment.setDataType(FileHelper.getInstance().getBlackBoardAttachmentDataType(filePath));
        return blackBoardAttachment;
    }

    private boolean isEditTextEmpty(EditText editText){
        String editString = editText.getText().toString();
        if (editString.matches("")) {
            return true;
        }
        return false;
    }

    private void resetGui() {
        editTextTitle.setText("");
        editTextDescription.setText("");
        autoCompleteTextViewContact.setText("");
        editTextFullName.setText("");
        editTextEmail.setText("");
        editTextTelephone.setText("");
        editTextCompany.setText("");
        editTextStreet.setText("");
        editTextCity.setText("");

        blackBoardAttachments = new ArrayList<>();
        blackBoardAttachmentsCopy = new ArrayList<>();
        blackBoardAttachmentViews = new ArrayList<>();
        blackBoardAttachmentViewsCopy = new ArrayList<>();

        attachmentContainer.removeAllViews();
    }

    @Override
    public void OnResponse(JSONObject response) {
        Gson gsonInstance = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        BlackBoardItem serverBlackBoardItem = gsonInstance.fromJson(new JsonParser().parse(response.toString()), BlackBoardItem.class);
        serverBlackBoardItem.setSyncStatus(true);
        BlackBoardItemDAO blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();
        blackBoardItemDAO.deleteByID(blackBoardItem.getBlackBoardItemID());
        blackBoardItemDAO.createOrUpdate(serverBlackBoardItem);

        if (mOnSaveBlackBoardItemListener != null) {
            mOnSaveBlackBoardItemListener.onSaveBlackBoardItem(true);
        }
    }

    @Override
    public void OnErrorResponse(VolleyError error) {
        if (mOnSaveBlackBoardItemListener != null) {
            mOnSaveBlackBoardItemListener.onSaveBlackBoardItem(false);
        }
    }

    public void setOnSaveBlackBoardItem(OnSaveBlackBoardItemListener onSaveBlackBoardItemListener){
        mOnSaveBlackBoardItemListener = onSaveBlackBoardItemListener;
    }

    public interface OnSaveBlackBoardItemListener{
        void onSaveBlackBoardItem(boolean isSuccessful);
    }

    public void setOnStartActivityResultListener(OnStartActivityResultListener onStartActivityResultListener){
        mOnStartActivityResultListener = onStartActivityResultListener;
    }

    public interface OnStartActivityResultListener{
        void onStartActivityResultListener();
    }
}
