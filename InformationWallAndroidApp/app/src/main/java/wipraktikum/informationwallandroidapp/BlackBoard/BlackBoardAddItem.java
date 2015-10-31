package wipraktikum.informationwallandroidapp.BlackBoard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment {

    private static BlackBoardAddItem instance = null;

    public static BlackBoardAddItem getInstance(){
        if (instance==null){
            instance = new BlackBoardAddItem();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_add_item, viewGroup, false);
        return view;
    }


}
