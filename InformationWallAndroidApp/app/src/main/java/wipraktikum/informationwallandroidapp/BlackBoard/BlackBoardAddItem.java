package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardAutoCompleteTextViewContactAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.CustomView.BlackBoardAttachmentView;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.UploadManager;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.RealPathHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment implements BlackBoard.OnActivityResultListener{
    public static final String BLACK_BOARD_ITEM_ID_TAG = "blackBoardItemID";
    private OnSaveBlackBoardItemListener mOnSaveBlackBoardItemListener = null;

    private static BlackBoardAddItem instance = null;
    private BlackBoardAutoCompleteTextViewContactAdapter autoCompleteTextViewContactAdapter = null;
    private Contact selectedContact = null;
    private ArrayList<BlackBoardAttachment> blackBoardAttachments = new ArrayList<>();
    private ArrayList<View> blackBoardAttachmentViews = new ArrayList<>();
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
    private Button buttonAttachment = null;

    private static List<BlackBoardAttachment> uploadList = new ArrayList<>();

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
        setUpGUI(view);

        //Add Attachment to View
        ((BlackBoard)getActivity()).setOnActivityResultListener(this);
        blackBoardAttachments = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        //Write BlackBoardItem Information to View
        if (getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG) != 0){
            BlackBoardItem editBlackBoardItem = (BlackBoardItem) DAOHelper.getInstance().getBlackBoardItemDAO().queryForId(
                    getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
            setBlackBoardItemInformation(editBlackBoardItem);
            isEditedItem = true;
        }
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
            saveBlackBoardItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(Intent data) {
        String filePath = RealPathHelper.getInstance().getRealPathFromURI(data.getData());
        //Add filePath to LinearLayout below
        BlackBoardAttachment blackBoardAttachment = createNewAttachment(filePath);
        View attachmentView = addAttachmentToView(blackBoardAttachment);
        uploadAttachment(blackBoardAttachment, attachmentView);
        blackBoardAttachments.add(blackBoardAttachment);
        blackBoardAttachmentViews.add(attachmentView);
    }

    private void setUpGUI(View view){
        autoCompleteTextViewContactAdapter = new BlackBoardAutoCompleteTextViewContactAdapter(
                getActivity(), 0, DAOHelper.getInstance().getContactDAO().queryForAll());
        autoCompleteTextViewContact = (AutoCompleteTextView) view.findViewById(R.id.ac_tv_black_board_add_item_contact);
        autoCompleteTextViewContact.setAdapter(autoCompleteTextViewContactAdapter);
        autoCompleteTextViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = (Contact) autoCompleteTextViewContactAdapter.getItem(position);
            }
        });

        tlAddContact = (TableLayout) view.findViewById(R.id.tl_black_board_add_item_contact);
        tlAddContact.setVisibility(View.GONE);

        editTextFullName = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_full_name);
        editTextEmail= (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_email);
        editTextTelephone = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_telephone);
        editTextCompany = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_company);
        editTextStreet = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_street);
        editTextCity = (EditText) view.findViewById(R.id.edit_black_board_add_item_contact_city);

        editTextTitle = (EditText) view.findViewById(R.id.edit_black_board_add_item_title);
        editTextDescription = (EditText) view.findViewById(R.id.edit_black_board_add_item_description);

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
                FileHelper.getInstance().showPictureChooser(getActivity());
            }
        });

    }

    public void setBlackBoardItemInformation(BlackBoardItem blackBoardItem){
        editTextTitle.setText(blackBoardItem.getTitle());
        //autoCompleteTextViewContact.setSelection(autoCompleteTextViewContactAdapter.
        //        getPosition(blackBoardItem.getContact()));
        editTextDescription.setText(blackBoardItem.getDescriptionText());
        for (BlackBoardAttachment blackBoardAttachment : blackBoardItem.getBlackBoardAttachment()) {
            addAttachmentToView(blackBoardAttachment);
        }
    }

    public void saveBlackBoardItem(){
        if (!isEditTextEmpty(editTextTitle) && uploadList.isEmpty()) {
            BlackBoardItem newBlackBoardItem = new BlackBoardItem();
            Contact newContact;

            newBlackBoardItem.setCreatedTimestamp(Calendar.getInstance().getTime());
            newBlackBoardItem.setEditedTimestamp(Calendar.getInstance().getTime());
            newBlackBoardItem.setTitle(editTextTitle.getText().toString());
            newBlackBoardItem.setDescriptionText(editTextDescription.getText().toString());
            newBlackBoardItem.setBlackBoardAttachment(blackBoardAttachments);
            newBlackBoardItem.setUser(InfoWallApplication.getCurrentUser());

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
            newBlackBoardItem.setContact(newContact);

            //Check if item is new or a edit
            if (isEditedItem){
                newBlackBoardItem.setBlackBoardItemID(getArguments().getLong(BLACK_BOARD_ITEM_ID_TAG));
                DAOHelper.getInstance().getBlackBoardItemDAO().update(newBlackBoardItem);
            }else {
                DAOHelper.getInstance().getBlackBoardItemDAO().create(newBlackBoardItem);
            }
            // Set Attachments to the item again because in the create method we have to clean erase this reference ( because of ORMLite )
            newBlackBoardItem.setBlackBoardAttachment(blackBoardAttachments);
            JsonManager.getInstance().sendJson(ServerURLManager.NEW_BLACK_BOARD_ITEM_URL, newBlackBoardItem);

            if (mOnSaveBlackBoardItemListener != null) {
                mOnSaveBlackBoardItemListener.onSaveBlackBoardItem();
            }
        }else{
            int snackBarStringID = R.string.black_board_add_item_snackbar_upload_failure;

            if(isEditTextEmpty(editTextTitle)){
                snackBarStringID = R.string.black_board_add_item_snackbar_no_title;
            }else if (!uploadList.isEmpty()){
                snackBarStringID = R.string.black_board_add_item_snackbar_upload_in_progress;
            }

            Snackbar
                .make(getView(), snackBarStringID, Snackbar.LENGTH_LONG)
                .show();
        }
    }

    private View addAttachmentToView(BlackBoardAttachment attachment){
        LinearLayout attachmentContainer = (LinearLayout) getView().findViewById(R.id.ll_attachment_container);
        BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(getActivity(), attachment, false);
        attachmentContainer.addView(attachmentView);

        return attachmentView;
    }

    private void uploadAttachment(final BlackBoardAttachment blackBoardAttachment, final View attachmentView){
        UploadManager uploadManager =  UploadManager.getInstance();

        //Show Upload Progress
        ((BlackBoardAttachmentView)attachmentView).showProgressbar(true);
        uploadList.add(blackBoardAttachment);
        //Upload File
        File attachmentFile = new File(blackBoardAttachment.getDeviceDataPath());
        uploadManager.uploadFile(attachmentFile, ServerURLManager.UPLOAD_BLACK_BOARD_ATTACHMENT_URL);
        uploadManager.setOnUploadFinishedListener(new UploadManager.OnUploadFinishedListener() {
            @Override
            public void onUploadFinished(String remoteDataPath) {
                //Show Upload has finished
                ((BlackBoardAttachmentView) attachmentView).showProgressbar(false);
                uploadList.remove(blackBoardAttachment);
                //Save remoteDataPath to attachment
                blackBoardAttachment.setRemoteDataPath(remoteDataPath);
            }
        });
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

    public void setOnSaveBlackBoardItem(OnSaveBlackBoardItemListener OnSaveBlackBoardItemListener){
        mOnSaveBlackBoardItemListener = OnSaveBlackBoardItemListener;
    }

    public interface OnSaveBlackBoardItemListener{
        public void onSaveBlackBoardItem();
    }
}
