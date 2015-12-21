package wipraktikum.informationwallandroidapp.Feedreader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.BusinessObject.FeedReader.Feed;
import wipraktikum.informationwallandroidapp.ServerCommunication.JsonManager;

/**
 * Created by Eric Schmidt on 21.12.2015.
 */
public class RSSSearch {
    private static final String feedlyStartURLString = "http://cloud.feedly.com/v3/search/feeds?q=";

    private OnSearchResponseReceiveListener mOnSearchResponseReceiveListener;

    public void searchRSSByName(String rssName){
        String feedlyURL = buildFeedlyURL(rssName);
        JsonManager rssSearch = new JsonManager();
        rssSearch.getJson(feedlyURL);
        rssSearch.setOnObjectResponseReceiveListener(new JsonManager.OnObjectResponseListener() {
            @Override
            public void OnResponse(JSONObject response) {
                if (mOnSearchResponseReceiveListener != null) {
                    mOnSearchResponseReceiveListener.OnSearchResponse(convertToFeedList(response));
                }
            }
        });
        //http://cloud.feedly.com/v3/search/feeds?q=http%3A%2F%2Fspiegel%2F&n=20&ck=1381664838936
    }

    private String buildFeedlyURL(String rssName){
        return feedlyStartURLString + rssName;
    }

    public void setOnSearchResponseReceiveListener(OnSearchResponseReceiveListener onSearchResponseReceiveListener){
        mOnSearchResponseReceiveListener = onSearchResponseReceiveListener;
    }

    private ArrayList<Feed> convertToFeedList(JSONObject response){
        ArrayList<Feed> feedList = new ArrayList<>();

        try {
            JSONArray feedJsonArray = response.getJSONArray("results");

            for (int i = 0; i < feedJsonArray.length(); i++) {
                JSONObject feedJsonObject = feedJsonArray.getJSONObject(i);
                Feed feed = new Feed();

                feed.setTitle(feedJsonObject.getString("title"));
                feed.setFeed(feedJsonObject.getString("feedId"));
                feed.setWebsite(feedJsonObject.getString("website"));
                feed.setDescription(feedJsonObject.getString("description"));

                feedList.add(feed);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return feedList;
    }

    public interface OnSearchResponseReceiveListener {
        void OnSearchResponse(ArrayList<Feed> feedList);
    }
}
