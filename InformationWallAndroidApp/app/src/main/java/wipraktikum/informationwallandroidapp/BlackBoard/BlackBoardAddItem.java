package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardAutoCompleteTextViewContactAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.BlackBoardUtils.BlackBoardAnimationUtils;
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
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.UploadManager;
import wipraktikum.informationwallandroidapp.Utils.ActivityHelper;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.RealPathHelper;
import wipraktikum.informationwallandroidapp.Utils.StringHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment implements BlackBoard.OnActivityResultListener, TextWatcher,
        JsonManager.OnObjectResponseListener, JsonManager.OnErrorListener {
    public static final String BLACK_BOARD_ITEM_ID_TAG = "blackBoardItemID";
    private static final String BLACK_BOARD_ATTACHMENT_SAVED_INSTANCE_TAG = "blackBoardAttachmentJSON";

    BlackBoardItemDAO blackBoardItemDAO;

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
    private boolean isFilePickerVisible = false;

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
    private Button buttonAddLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_add_item, viewGroup, false);
        setHasOptionsMenu(true);

        blackBoardItemDAO = DAOHelper.getInstance().getBlackBoardItemDAO();

        ((BlackBoard)getActivity()).setOnActivityResultListener(this);
        setRetainInstance(true);

        setUpGUI(view);

        //Write BlackBoardItem Information to View
        if (getArguments() != null){
            blackBoardItem = (BlackBoardItem) blackBoardItemDAO.queryForId(
                    getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
            isEditedItem = true;
            fillInBlackBoardItemUI();
        }else{
            blackBoardItem = new BlackBoardItem();
        }

        return view;
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
                isFilePickerVisible = true;
                FileHelper.getInstance().showFileChooser(getActivity());
            }
        });

        buttonAddLayout = (Button) view.findViewById(R.id.b_black_board_add_layout);
        buttonAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.openLayoutSelectionwActivity(InfoWallApplication.getInstance());
            }
        });

        attachmentContainer = (LinearLayout) view.findViewById(R.id.ll_attachment_container);
    }

    private void fillInBlackBoardItemUI() {
        String title = getBlackBoardItem().getTitle();
        String description = getBlackBoardItem().getDescriptionText();
        Contact contact = getBlackBoardItem().getContact();
        blackBoardAttachments = getBlackBoardItem().getBlackBoardAttachment();

        editTextTitle.setText(title);
        editTextDescription.setText(description);
        fillInContactUI(contact);
        fillInAttachmentUI();

    }
    
    private void fillInContactUI(Contact contact){
        if (contact != null) {
            autoCompleteTextViewContact.setText(contact.getFullName());
            selectedContact = contact;
        }
    }

    private void fillInAttachmentUI() {
        setAttachmentViewContent();
    }

    @Override
    public void onResume(){
        super.onResume();
        //Tell Server to open Live Preview
        BlackBoardAnimationUtils.openLivePreview();

        //Handle initial Live Preview
        sendLivePreviewToServer();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //Retrieve SavedInstance
        if (savedInstanceState != null) {
            setCurrentAttachmentsFromInstanceState(savedInstanceState);
            setAttachmentViewContent();
        }
    }

    private void setCurrentAttachmentsFromInstanceState(Bundle savedInstanceState){
        Gson gsonInstance = new GsonBuilder().create();
        String jsonString = savedInstanceState.getString(BLACK_BOARD_ATTACHMENT_SAVED_INSTANCE_TAG);
        blackBoardAttachments = gsonInstance.fromJson(jsonString, new TypeToken<List<BlackBoardAttachment>>() {}.getType());
    }

    @Override
    public void onPause() {
        super.onPause();

        removeErrorsFromTextFields();

        if (!isFilePickerVisible) {
            BlackBoardAnimationUtils.closeLivePreviewOnServer();
        }else{
            isFilePickerVisible = false;
        }
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
                saveBlackBoardItem();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateInputs() {
        boolean valid = true;
        validateTitleInput(valid);
        validateContactInput(valid);
        return valid;
    }

    private void validateTitleInput(boolean valid) {
        if(StringHelper.isStringNullOrEmpty(editTextTitle.getText().toString())){
            editTextTitle.setError("enter a valid email address");
            valid = false;
        }else {
            editTextTitle.setError(null);
        }
    }

    private void validateContactInput(boolean valid) {
        if(tlAddContact.getVisibility() != View.GONE){
            String email = editTextEmail.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("enter a valid email address");
                valid = false;
            } else {
                editTextEmail.setError(null);
            }

            if(StringHelper.isStringNullOrEmpty(editTextFullName.getText().toString())) {
                editTextFullName.setError("enter a name");
                valid = false;
            } else {
                editTextFullName.setError(null);
            }
        }
    }

    private void saveBlackBoardItem(){
            blackBoardAttachmentsCopy.addAll(blackBoardAttachments);
            blackBoardAttachmentViewsCopy.addAll(blackBoardAttachmentViews);
            uploadAttachment();
    }

    @Override
    public void onActivityResult(Intent data) {

        //Add filePath to LinearLayout below
        BlackBoardAttachment blackBoardAttachment = BlackBoardAttachment.createNewAttachmentByFilePath(
                getFilePathFromResult(data)
        );
        addAttachmentViewToAttachmentContainer(blackBoardAttachment);
        blackBoardAttachments.add(blackBoardAttachment);

        isFilePickerVisible = false;
    }

    private String getFilePathFromResult(Intent data) {
        //ContactHelper.createVCFFromUri(getActivity(), data.getData());
        return RealPathHelper.getInstance().getRealPathFromURI(data.getData());
    }

    private View addAttachmentViewToAttachmentContainer(BlackBoardAttachment attachment){
        BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(getActivity(), attachment, false);
        attachmentContainer.addView(attachmentView, 0);
        blackBoardAttachmentViews.add(attachmentView);
        return attachmentView;
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
        saveBlackBoardAttachmentsToInstanceState(outState);
    }

    /**
     * save BlackBoardAttachments to instance state because otherwise attachment views get lost after onPause
     */
    private void saveBlackBoardAttachmentsToInstanceState(Bundle outState) {
        outState.putString(
                BLACK_BOARD_ATTACHMENT_SAVED_INSTANCE_TAG,
                BlackBoardAttachment.convertAttachmentListToJson(blackBoardAttachments)
        );
    }

    private void setAttachmentViewContent(){
        attachmentContainer.removeAllViews();
        if (blackBoardAttachments != null) {
            for (BlackBoardAttachment blackBoardAttachment : blackBoardAttachments) {
                addAttachmentViewToAttachmentContainer(blackBoardAttachment);
            }
        }
    }

    private void sendLivePreviewToServer(){
        if (blackBoardItem != null) {
           BlackBoardAnimationUtils.sendLivePreviewUpdate(fillBlackBoardItem());
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
            newContact = selectedContact;
        }
        blackBoardItem.setContact(newContact);

        return blackBoardItem;
    }

    //Needs a better name
    private void uploadAttachment(){
        while (!blackBoardAttachmentsCopy.isEmpty() && blackBoardAttachmentsCopy.get(0).getRemoteDataPath() != null){
            blackBoardAttachmentsCopy.remove(0);
            blackBoardAttachmentViewsCopy.remove(0);
        }

        //Check again if the attachments are null
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
            saveBlackBoardItemToDB();
            sendBlackBoardItemToServer();
        }
    }

    private void saveBlackBoardItemToDB(){
        fillBlackBoardItem();
        //Check if item is a edit
        if (isEditedItem){
            blackBoardItem.setSyncStatus(true);
            blackBoardItem.setBlackBoardItemID(getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
        }
        blackBoardAttachments = blackBoardItem.getBlackBoardAttachment();
        blackBoardItemDAO.createOrUpdate(blackBoardItem);

    }

    private void sendBlackBoardItemToServer() {
        JsonManager jsonManager = new JsonManager();
        jsonManager.setOnObjectResponseReceiveListener(this);
        jsonManager.setOnErrorReceiveListener(this);
        jsonManager.sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, blackBoardItem);
    }

    private Contact createNewContact(){
        Contact newContact = new Contact();

        if (!StringHelper.isStringNullOrEmpty(editTextFullName.getText().toString())) {
            Contact.splitContactFullName(newContact, editTextFullName.getText().toString());
        }
        if (!StringHelper.isStringNullOrEmpty(editTextFullName.getText().toString()))
            newContact.setTelephone(editTextTelephone.getText().toString());
        if (!StringHelper.isStringNullOrEmpty((editTextEmail.getText().toString())))
            newContact.setEMailAddress(editTextEmail.getText().toString());
        if (!StringHelper.isStringNullOrEmpty((editTextCompany.getText().toString())))
            newContact.setCompany(editTextCompany.getText().toString());

        ContactAddress newContactAddress = new ContactAddress();

        if (!StringHelper.isStringNullOrEmpty((editTextStreet.getText().toString()))) {
            ContactAddress.splitStreet(newContactAddress, editTextStreet.getText().toString());
        }
        if (!StringHelper.isStringNullOrEmpty((editTextCity.getText().toString()))) {
            ContactAddress.splitCity(newContactAddress, editTextCity.getText().toString());
        }
        newContact.setContactAddress(newContactAddress);

        return newContact;
    }



    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ActivityHelper.hideKeyboard(getActivity().getCurrentFocus());
    }

    @Override
    public void OnResponse(JSONObject response) {
        BlackBoardAnimationUtils.closeLivePreviewOnServer();
        updateBlackBoardItemInDB(response);
        triggerOnSaveBlackBoardItemEvent(true);
    }

    private void updateBlackBoardItemInDB(JSONObject response) {
        BlackBoardItem serverBlackBoardItem = BlackBoardItem.parseItemFromJson(response.toString());
        serverBlackBoardItem.setSyncStatus(true);
        blackBoardItemDAO.delete(blackBoardItem);
        blackBoardItemDAO.createOrUpdate(serverBlackBoardItem);
    }

    private void triggerOnSaveBlackBoardItemEvent(boolean successfull) {
        if (mOnSaveBlackBoardItemListener != null) {
            mOnSaveBlackBoardItemListener.onSaveBlackBoardItem(successfull);
        }
    }

    @Override
    public void OnErrorResponse(VolleyError error) {
        BlackBoardAnimationUtils.closeLivePreviewOnServer();
        triggerOnSaveBlackBoardItemEvent(false);
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
