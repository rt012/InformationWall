package wipraktikum.informationwallandroidapp.Utils;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact;

/**
 * Created by Eric Schmidt on 31.10.2015.
 */
public class ContactFilter extends Filter{
    private List<Contact> mContacts = null;
    private List<Contact> suggestions = null;
    private ArrayAdapter adapter = null;

    public ContactFilter(ArrayAdapter adapter, List<Contact> contacts){
        this.adapter = adapter;
        this.mContacts = contacts;
        this.suggestions = new ArrayList<>();
    }

    @Override
    public String convertResultToString(Object resultValue) {
        String contactFullName = ((Contact)(resultValue)).getFullName();
        return contactFullName;
    }
    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        if(constraint != null) {
            suggestions.clear();

            for (Contact contact : mContacts) {
                if(contact.getFullName().toLowerCase().contains(constraint.toString().toLowerCase())){
                    suggestions.add(contact);
                }
            }

            Filter.FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        } else {
            return new Filter.FilterResults();
        }
    }
    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        ArrayList<Contact> filteredList = (ArrayList<Contact>) results.values;
        if(results != null && results.count > 0) {
            adapter.clear();
            for (Contact contact : filteredList) {
                adapter.add(contact);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
