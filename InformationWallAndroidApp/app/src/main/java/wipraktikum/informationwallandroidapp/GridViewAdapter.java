package wipraktikum.informationwallandroidapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Remi on 18.10.2015.
 */
public final class GridViewAdapter extends BaseAdapter {
    private final BaseActivity context;
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public GridViewAdapter(BaseActivity context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);

        mItems.add(new Item("Example Tile 1",       R.drawable.slide_1, MainActivity.class));
        mItems.add(new Item("Example Tile 2",   R.drawable.slide_2,  MainActivity.class));
        mItems.add(new Item("Example Tile 3", R.drawable.slide_3,  MainActivity.class));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        final Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.startActivity(new Intent(context, item.screen));
            }
        });

        picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new GridViewLongClickDialog().show(context.getSupportFragmentManager(), "GridViewLongClickDialog");
                return false;
            }
        });

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;
        public final Class screen;

        Item(String name, int drawableId, Class screen) {
            this.name = name;
            this.drawableId = drawableId;
            this.screen = screen;
        }
    }
}
