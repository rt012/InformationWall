package wipraktikum.informationwallandroidapp.TileOverview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import wipraktikum.informationwallandroidapp.BusinessObject.Tile;
import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBTile;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 18.10.2015.
 */
public final class GridViewAdapter extends BaseAdapter {
    private final BaseActivity context;
    private final LayoutInflater mInflater;
    private List<Tile> mTiles = new ArrayList<Tile>();

    public GridViewAdapter(BaseActivity context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);

        // Get tiles from database
        mTiles = DAOHelper.getInstance().getTileDAO().queryForAll();
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
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
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

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    context.startActivity(new Intent(context, Class.forName(tile.getScreen())));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // Supply num input as an argument.
                Bundle args = new Bundle();
                args.putString("tileTitle", tile.getName());
                args.putBoolean("isActivated", tile.getIsActivated());
                args.putInt("radioButtonPos", tile.getTileSize().ordinal());

                // Open LongClickDialog
                GridViewLongClickDialog gridViewLongClickDialog = new GridViewLongClickDialog();
                gridViewLongClickDialog.setArguments(args);
                gridViewLongClickDialog.show(context.getSupportFragmentManager(), "GridViewLongClickDialog");

                // Listeners
                gridViewLongClickDialog.setOnSwitchChangeListener(new GridViewLongClickDialog.OnSwitchChangeListener() {
                    @Override
                    public void onSwitchChanged(boolean isChecked) {
                        Log.i("GridViewAdapter", "Send message to Webserver - Tile activated");
                        if (isChecked) {
                            mRelativeLayout.setVisibility(View.VISIBLE);
                            tile.setIsActivated(true);
                        } else {
                            mRelativeLayout.setVisibility(View.GONE);
                            tile.setIsActivated(false);
                        }
                        DAOHelper.getInstance().getTileDAO().update(tile);
                    }
                });
                gridViewLongClickDialog.setOnRadioButtonChangeListener(new GridViewLongClickDialog.OnRadioButtonChangeListener() {
                    @Override
                    public void onRadioButtonChanged(int radioButtonPos) {
                        Log.i("GridViewAdapter", "Send message to Webserver - Tile changed Size");
                        tile.setTileSize(DBTile.EnumTileSize.values()[radioButtonPos]);
                        DAOHelper.getInstance().getTileDAO().update(tile);
                    }
                });

                return false;
            }
        });

        return v;
    }
}

