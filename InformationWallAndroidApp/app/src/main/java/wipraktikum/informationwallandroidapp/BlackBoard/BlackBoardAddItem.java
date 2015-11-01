package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Date;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardAutoCompleteTextViewContactAdapter;
import wipraktikum.informationwallandroidapp.BlackBoard.CustomView.BlackBoardAttachmentView;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardAttachment;
import wipraktikum.informationwallandroidapp.BusinessObject.BlackBoard.BlackBoardItem;
import wipraktikum.informationwallandroidapp.BusinessObject.Contact;
import wipraktikum.informationwallandroidapp.BusinessObject.ContactAddress;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.BlackBoard.DBBlackBoardAttachment;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.UploadManager;
import wipraktikum.informationwallandroidapp.Utils.FileHelper;
import wipraktikum.informationwallandroidapp.Utils.RealPathHelper;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment implements IFragmentTag{
    private final String FRAGMENT_TAG = "FRAGMENT_ADD_ITEM";
    private OnSaveBlackBoardItemListener mOnSaveBlackBoardItemListener = null;

    private static BlackBoardAddItem instance = null;
    private BlackBoardAutoCompleteTextViewContactAdapter autoCompleteTextViewContactAdapter = null;
    private Contact selectedContact = null;
    private ArrayList<BlackBoardAttachment> blackBoardAttachments = new ArrayList<>();
    private ArrayList<View> blackBoardAttachmentViews = new ArrayList<>();

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
        ((BlackBoard)getActivity()).setOnActivityResultListener(new BlackBoard.OnActivityResultListener() {
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
        });

        blackBoardAttachments = new ArrayList<>();

        return view;
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

    public void saveBlackBoardItem(){
        BlackBoardItem newBlackBoardItem = new BlackBoardItem();
        Contact newContact;

        newBlackBoardItem.setCreatedTimestamp(new Date());
        newBlackBoardItem.setEditedTimestamp(new Date());
        newBlackBoardItem.setTitle(editTextTitle.getText().toString());
        newBlackBoardItem.setDescriptionText(editTextDescription.getText().toString());

        //Check if a new contact was created (LinearLayout.View(Visible)) or a existing was selected
        if (tlAddContact.getVisibility() == View.VISIBLE){
            newContact = createNewContact();
        }else{
            if (selectedContact == null){
                selectedContact = new Contact();
                selectedContact.setContactAddress(new ContactAddress());
            }
            newContact = selectedContact;
        }
        newBlackBoardItem.setContact(newContact);
        DAOHelper.getInstance().getBlackBoardItemDAO().create(newBlackBoardItem);

        //Create each Attachment and connect it to the black board item
        for (BlackBoardAttachment blackBoardAttachment : blackBoardAttachments){
            blackBoardAttachment.setBlackBoardItem(DAOHelper.getInstance().
                    getBlackBoardItemDAO().mapBlackBoardItemToDBBlackBoardItem(newBlackBoardItem));
            DAOHelper.getInstance().getBlackBoardAttachmentDAO().create(blackBoardAttachment);
        }

        JsonManager.getInstance().sendJson(newBlackBoardItem, JsonManager.NEW_BLACK_BOARD_ITEM_URL);

        if(mOnSaveBlackBoardItemListener != null){
            mOnSaveBlackBoardItemListener.onSaveBlackBoardItem();
        }
    }

    private View addAttachmentToView(BlackBoardAttachment attachment){
        LinearLayout attachmentContainer = (LinearLayout) getView().findViewById(R.id.ll_attachment_container);
        attachmentContainer.removeAllViews();
        BlackBoardAttachmentView attachmentView = new BlackBoardAttachmentView(getActivity(), attachment, false);
        attachmentContainer.addView(attachmentView);

        return attachmentView;
    }

    private void uploadAttachment(BlackBoardAttachment blackBoardAttachment, final View attachmentView){
        UploadManager uploadManager =  UploadManager.getInstance();
        ((BlackBoardAttachmentView)attachmentView).showProgressbar(true);
        File attachmentFile = new File(blackBoardAttachment.getDeviceDataPath());
        uploadManager.uploadFile(attachmentFile, "http://myinfowall.ddns.net/phpTest2.php");
        uploadManager.setOnUploadFinishedListener(new UploadManager.OnUploadFinishedListener() {
            @Override
            public void onUploadFinished(boolean success) {
                ((BlackBoardAttachmentView) attachmentView).showProgressbar(false);
            }
        });
    }

    private Contact createNewContact(){
        Contact newContact = new Contact();

        String[] splitFullName = editTextFullName.getText().toString().split(" ");
        if (splitFullName.length == 2) {
            newContact.setSurname(splitFullName[0]);
            newContact.setName(splitFullName[1]);
        }else{
            newContact.setSurname(splitFullName[0]);
        }

        newContact.setTelephone(editTextTelephone.getText().toString());
        newContact.setEMailAddress(editTextEmail.getText().toString());
        newContact.setCompany(editTextCompany.getText().toString());

        ContactAddress newContactAddress = new ContactAddress();

        String[] splitStreet = editTextStreet.getText().toString().split(" ");
        if (splitStreet.length == 2) {
            newContactAddress.setStreetName(splitStreet[0]);
            newContactAddress.setHouseNumber(splitStreet[1]);
        }else{
            newContactAddress.setStreetName(splitStreet[0]);
        }

        String[] splitCity = editTextCity.getText().toString().split(" ");
        if (splitStreet.length == 2) {
            newContactAddress.setZipCode(splitCity[0]);
            newContactAddress.setCity(splitCity[1]);
        }else{
            newContactAddress.setCity(splitCity[0]);
        }

        newContact.setContactAddress(newContactAddress);

        return newContact;
    }

    private BlackBoardAttachment createNewAttachment(String filePath){
        BlackBoardAttachment blackBoardAttachment = new BlackBoardAttachment();
        blackBoardAttachment.setDeviceDataPath(filePath);
        blackBoardAttachment.setDataType(DBBlackBoardAttachment.DataType.IMG);
        return blackBoardAttachment;
    }

    public String getCustomTag(){
        return this.FRAGMENT_TAG;
    }

    public void setOnSaveBlackBoardItem(OnSaveBlackBoardItemListener OnSaveBlackBoardItemListener){
        mOnSaveBlackBoardItemListener = OnSaveBlackBoardItemListener;
    }

    public interface OnSaveBlackBoardItemListener{
        public void onSaveBlackBoardItem();
    }
}
