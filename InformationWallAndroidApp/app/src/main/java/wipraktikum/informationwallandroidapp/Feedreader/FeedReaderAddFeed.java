package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;
import wipraktikum.informationwallandroidapp.ServerCommunication.ServerURLManager;
import wipraktikum.informationwallandroidapp.Utils.JSONBuilder;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderAddFeed extends Fragment implements JsonManager.OnObjectResponseListener, JsonManager.OnErrorListener {
    private ListView rssList = null;
    private EditText rssSearch = null;

    private RSSSearch rssSearchManager = null;
    private Feed feed = null;
    private OnSaveFeedListener mOnSaveFeedListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reader_search, viewGroup, false);
        setHasOptionsMenu(true);

        setRetainInstance(true);

        ((FeedReader) getActivity()).showFab(false);
        rssSearchManager = new RSSSearch();

        initViews(view);

        return view;
    }

    private void initViews(View view){
        rssList = (ListView) view.findViewById(R.id.feed_reader_rss_list);
        rssList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                feed = (Feed) rssList.getAdapter().getItem(position);
                feed.setSyncStatus(false);
                saveFeedToDB(feed);
                saveFeedToServer(feed);
            }
        });


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

    private void saveFeedToDB(Feed feed){
        DAOHelper.getFeedReaderDAO().create(feed);
    }

    private void saveFeedToServer(Feed feed){
        JsonManager jsonManager = new JsonManager();
        JSONObject feedJson = JSONBuilder.createJSONFromObject(feed);
        jsonManager.sendJson(ServerURLManager.NEW_FEED_KEY, feedJson);
        jsonManager.setOnObjectResponseReceiveListener(this);
        jsonManager.setOnErrorReceiveListener(this);
    }

    private void updateFeedFromServer(JSONObject response) {
        Feed serverFeed = Feed.parseItemFromJson(response.toString());
        serverFeed.setSyncStatus(true);
        DAOHelper.getFeedReaderDAO().delete(feed);
        DAOHelper.getFeedReaderDAO().createOrUpdate(serverFeed);
    }

    @Override
    public void OnErrorResponse(VolleyError error) {
        triggerOnSaveFeedEvent(false);
    }

    @Override
    public void OnResponse(JSONObject response) {
        updateFeedFromServer(response);
        triggerOnSaveFeedEvent(true);
    }

    private void triggerOnSaveFeedEvent(boolean successfull) {
        if (mOnSaveFeedListener != null) {
            mOnSaveFeedListener.onSaveFeed(successfull);
        }
    }

    public void setOnSaveFeedListener(OnSaveFeedListener onSaveFeedListener){
        mOnSaveFeedListener = onSaveFeedListener;
    }

    public interface OnSaveFeedListener {
        void onSaveFeed(boolean isSuccessful);
    }

}
