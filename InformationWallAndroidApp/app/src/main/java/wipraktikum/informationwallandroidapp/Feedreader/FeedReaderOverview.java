package wipraktikum.informationwallandroidapp.Feedreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.Feedreader.Adapter.FeedReaderListAdapter;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 28.12.2015.
 */
public class FeedReaderOverview extends Fragment {
    private ListView rssList = null;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reader_overview, viewGroup, false);
        setHasOptionsMenu(true);

        setRetainInstance(true);
        initViews(view);
        ((FeedReader) getActivity()).showFab(true);

        return view;
    }

    private void initViews(View view){
        rssList = (ListView) view.findViewById(R.id.feed_reader_rss_list);
        rssList.setEmptyView(view.findViewById(R.id.empty));
        fillRSSList();
    }


    private void fillRSSList(){
        rssList.setAdapter(new FeedReaderListAdapter(getActivity(), 0, DAOHelper.getFeedReaderDAO().queryForAll()));
    }

}
