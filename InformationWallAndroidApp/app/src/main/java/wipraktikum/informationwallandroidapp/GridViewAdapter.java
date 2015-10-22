package wipraktikum.informationwallandroidapp;

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

/**
 * Created by Remi on 18.10.2015.
 */
public final class GridViewAdapter extends BaseAdapter {
    private final BaseActivity context;
    private final List<Tile> mTiles = new ArrayList<Tile>();
    private final LayoutInflater mInflater;

    public GridViewAdapter(BaseActivity context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);

        mTiles.add(new Tile("Example Tile 1", R.drawable.slide_1, MainActivity.class));
        mTiles.add(new Tile("Example Tile 2", R.drawable.slide_2,  MainActivity.class));
        mTiles.add(new Tile("Example Tile 3", R.drawable.slide_3,  MainActivity.class));
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

        picture.setImageResource(tile.getDrawableId());
        name.setText(tile.getName());

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.startActivity(new Intent(context, tile.getScreen()));
            }
        });

        picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // Supply num input as an argument.
                Bundle args = new Bundle();
                args.putString("tileTitle", tile.getName());
                args.putBoolean("isActivated", tile.getIsActivated());
                args.putInt("radioButtonPos", tile.getTileSize().getValue());

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
                            tile.setIsActivated(true);
                        }
                    }
                });
                gridViewLongClickDialog.setOnRadioButtonChangeListener(new GridViewLongClickDialog.OnRadioButtonChangeListener() {
                    @Override
                    public void onRadioButtonChanged(int radioButtonPos) {
                        Log.i("GridViewAdapter", "Send message to Webserver - Tile changed Size");
                        tile.setTileSize(EnumTileSize.fromInt(radioButtonPos));
                    }
                });

                return false;
            }
        });

        return v;
    }
}

