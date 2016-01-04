package wipraktikum.informationwallandroidapp.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 02.01.2016.
 */
public class UndoDeleteHelper {

    private OnUndoDeleteListener mOnUndoDeleteListener = null;
    private Context mContext = null;
    private View mRootView = null;
    private ArrayList<Object> deletedObjects = null;

    public UndoDeleteHelper(View rootView, Context context){
        mRootView = rootView;
        mContext = context;
        deletedObjects = new ArrayList<>();
    }

    public void showUndoSnackbar(final Object deletedObject){
        deletedObjects.add(deletedObject);

        Snackbar.make(mRootView, deletedObjects.size() + " " + mContext.getString(R.string.undo_delete_items), Snackbar.LENGTH_LONG).
                setAction(mContext.getString(R.string.undo_delete), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnUndoDeleteListener != null){
                            mOnUndoDeleteListener.onUndo(deletedObjects);
                        }
                        deletedObjects.clear();
                    }
                }).show();
    }

    public void setOnUndoDeleteListener(OnUndoDeleteListener onUndoDeleteListener){
        mOnUndoDeleteListener = onUndoDeleteListener;
    }

    public interface OnUndoDeleteListener{
        void onUndo(ArrayList<Object> undoObjects);
    }
}
