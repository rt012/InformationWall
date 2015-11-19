package wipraktikum.informationwallandroidapp.Utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 31.10.2015.
 */
public class ContactFilter extends Filter{
    private List<Contact> mContacts = null;
    private ArrayList<Contact> mContactsAll = null;
    private List<Contact> suggestions = null;
    private ArrayAdapter adapter = null;

    private static String search;

    public ContactFilter(ArrayAdapter adapter, List<Contact> contacts){
        this.adapter = adapter;
        this.mContacts = contacts;
        this.mContactsAll = new ArrayList<>(contacts);
        this.suggestions = new ArrayList<>();
    }

    @Override
    public String convertResultToString(Object resultValue) {
        String contactFullName = ((Contact)(resultValue)).getFullName();
        return contactFullName;
    }
    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint != null) {
            search = constraint.toString();
            suggestions.clear();

            for (Contact contact : mContactsAll) {
                if (contact.getFullName() != null) {
                    if (contact.getFullName().toLowerCase().contains(search.toLowerCase())) {
                        suggestions.add(contact);
                    }
                }
            }

            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
        } else {
            filterResults.values = mContactsAll;
            filterResults.count = mContactsAll.size();
        }
        return filterResults;
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

    public static CharSequence highlight(String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer
                .normalize(originalText, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.getDefault());

        int start = normalizedText.indexOf(search.toLowerCase(Locale.getDefault()));
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(),
                        originalText.length());

                highlighted.setSpan(new ForegroundColorSpan(InfoWallApplication.getInstance().getResources().getColor(R.color.ci_color_light)),
                        spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                highlighted.setSpan(new StyleSpan(Typeface.BOLD),
                        spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }

}
