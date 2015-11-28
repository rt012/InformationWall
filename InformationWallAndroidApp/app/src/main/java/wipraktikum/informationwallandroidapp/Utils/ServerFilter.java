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

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 27.11.2015.
 */
public class ServerFilter extends Filter {
    private List<String> mServerURLList = null;
    private ArrayList<String> mServerURLListAll = null;
    private List<String> suggestions = null;
    private ArrayAdapter adapter = null;

    private static String search;

    public ServerFilter(ArrayAdapter adapter, List<String> serverURLList){
        this.adapter = adapter;
        this.mServerURLList = serverURLList;
        this.mServerURLListAll = new ArrayList<>(serverURLList);
        this.suggestions = new ArrayList<>();
    }

    @Override
    public String convertResultToString(Object resultValue) {
        if (resultValue != null) {
            return resultValue.toString();
        }
        return null;
    }



    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint != null) {
            search = constraint.toString();
            suggestions.clear();

            for (String serverURL : mServerURLListAll) {
                if (serverURL != null) {
                    if (serverURL.toLowerCase().contains(search.toLowerCase())) {
                        suggestions.add(serverURL);
                    }
                }
            }

            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
        } else {
            filterResults.values = mServerURLListAll;
            filterResults.count = mServerURLListAll.size();
        }
        return filterResults;
    }
    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        ArrayList<String> filteredList = (ArrayList<String>) results.values;
        if(results != null && results.count > 0) {
            adapter.clear();
            for (String serverURL : filteredList) {
                adapter.add(serverURL);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public static CharSequence highlight(String originalText) {
        if (!StringHelper.isStringNullOrEmpty(search)) {
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
        return originalText;
    }

}
