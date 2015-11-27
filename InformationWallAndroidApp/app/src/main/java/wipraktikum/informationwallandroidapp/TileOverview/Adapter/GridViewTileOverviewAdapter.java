package wipraktikum.informationwallandroidapp.TileOverview.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wipraktikum.informationwallandroidapp.BaseActivity;
import wipraktikum.informationwallandroidapp.BusinessObject.Tile.Tile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 18.10.2015.
 */
public final class GridViewTileOverviewAdapter extends BaseAdapter {
    private final BaseActivity context;
    private final LayoutInflater mInflater;
    private List<Tile> mTiles = new ArrayList<Tile>();

    public GridViewTileOverviewAdapter(BaseActivity context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);

        // Get tiles from database
        mTiles = DAOHelper.getTileDAO().queryForAll();
    }

    @Override
    public int getCount() {
        return mTiles.size();
    }

    @Override
    public Tile getItem(int i) {
        return mTiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mTiles.get(i).getDrawableId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        final ImageView picture;
        TextView name;
        final RelativeLayout mRelativeLayout;

        if (v == null) {
            v = mInflater.inflate(R.layout.tile_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.tileBorder);

        final Tile tile = getItem(i);

        if(tile.getIsActivated()) {
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            mRelativeLayout.setVisibility(View.GONE);
        }

        picture.setImageResource(tile.getDrawableId());
        name.setText(tile.getName());

        return v;
    }
}

