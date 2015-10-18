package wipraktikum.informationwallandroidapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Switch;

/**
 * Created by Remi on 18.10.2015.
 */
public class GridViewLongClickDialog extends DialogFragment {

    private RadioGroup radioGroup;
    private Switch switchActivate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_gridviewlongclick, container);
        switchActivate = (Switch) view.findViewById(R.id.switch_showItem);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_itemSize);
        int selectedId = radioGroup.getCheckedRadioButtonId();


        return view;
    }

}