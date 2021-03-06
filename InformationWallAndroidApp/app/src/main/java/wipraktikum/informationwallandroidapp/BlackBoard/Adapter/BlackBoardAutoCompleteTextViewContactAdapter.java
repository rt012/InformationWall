package wipraktikum.informationwallandroidapp.BlackBoard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.ContactFilter;

/**
 * Created by Eric Schmidt on 31.10.2015.
 */
public class BlackBoardAutoCompleteTextViewContactAdapter extends ArrayAdapter {

    private List<Contact> mContacts = null;
    private List<Contact> mContactsTemp = null;
    private Context mContext = null;
    private ContactFilter mContactFilter = null;

    public BlackBoardAutoCompleteTextViewContactAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);

        this.mContacts = contacts;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView = view;
        Contact contact = (Contact) getItem(position);

        if (convertView == null){
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.contact_item, viewGroup, false);
        }

        TextView textViewFullName = (TextView) convertView.
                findViewById(R.id.tv_black_board_add_item_adapter_contact_full_name);
        textViewFullName.setText(mContactFilter.highlight(contact.getFullName()));

        TextView textViewFullAddress = (TextView) convertView.
                findViewById(R.id.tv_black_board_add_item_adapter_contact_email);
        textViewFullAddress.setText(contact.getEMailAddress());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mContactFilter == null)
            mContactFilter = new ContactFilter(this, mContacts);

        return mContactFilter;
    }
}
