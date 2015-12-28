package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderAddFeed extends Fragment {
    private View mRootView = null;
    private ListView rssList = null;
    private EditText rssSearch = null;

    RSSSearch rssSearchManager = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reader, viewGroup, false);
        setHasOptionsMenu(true);

        setRetainInstance(true);

        rssSearchManager = new RSSSearch();

        initViews(view);

        return view;
    }

    private void initViews(View view){
        rssList = (ListView) view.findViewById(R.id.feed_reader_rss_list);

        rssSearch = (EditText) view.findViewById(R.id.input_rss_search);
        rssSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    rssSearchManager.searchRSSByName(s.toString());
                    rssSearchManager.setOnSearchResponseReceiveListener(new RSSSearch.OnSearchResponseReceiveListener() {
                        @Override
                        public void OnSearchResponse(ArrayList<Feed> response) {
                            fillRSSList(response);
                        }
                    });
                } else {
                    fillRSSList(new ArrayList<Feed>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fillRSSList(ArrayList<Feed> results){
        rssList.setAdapter(new FeedReaderListAdapter(getActivity(), 0, results));
    }
}
