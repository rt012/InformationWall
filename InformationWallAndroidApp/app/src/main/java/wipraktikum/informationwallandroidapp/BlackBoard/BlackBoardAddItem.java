package wipraktikum.informationwallandroidapp.BlackBoard;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import wipraktikum.informationwallandroidapp.BlackBoard.Adapter.BlackBoardAutoCompleteTextViewContactAdapter;
import wipraktikum.informationwallandroidapp.Database.DAO.DAOHelper;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class BlackBoardAddItem extends Fragment {

    private static BlackBoardAddItem instance = null;
    private Calendar calendar = null;

    private EditText editDate = null;
    private TableLayout tlAddContact = null;
    private AutoCompleteTextView autoCompleteTextViewContact = null;
    private ImageButton imageButtonContact = null;

    public static BlackBoardAddItem getInstance(){
        if (instance==null){
            instance = new BlackBoardAddItem();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_black_board_add_item, viewGroup, false);

        editDate = (EditText) view.findViewById(R.id.edit_black_board_add_item_date);
        editDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
               openDatePicker();
           }
        });
        calendar = Calendar.getInstance();

        autoCompleteTextViewContact = (AutoCompleteTextView) view.findViewById(R.id.ac_tv_black_board_add_item_contact);
        autoCompleteTextViewContact.setAdapter(
                new BlackBoardAutoCompleteTextViewContactAdapter(getActivity(), 0, DAOHelper.getInstance().getContactDAO().queryForAll()));

        tlAddContact = (TableLayout) view.findViewById(R.id.tl_black_board_add_item_contact);
        tlAddContact.setVisibility(View.GONE);

        imageButtonContact = (ImageButton) view.findViewById(R.id.ib_tv_black_board_add_item_contact);
        imageButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tlAddContact.getVisibility() == View.GONE) {
                    tlAddContact.setVisibility(View.VISIBLE);
                    autoCompleteTextViewContact.setEnabled(false);
                    autoCompleteTextViewContact.setText(getActivity().getString(R.string.black_board_add_item_new_contact));
                    imageButtonContact.setImageDrawable(getActivity().getDrawable(R.drawable.ic_arrow_up));
                }else{
                    tlAddContact.setVisibility(View.GONE);
                    autoCompleteTextViewContact.setEnabled(true);
                    autoCompleteTextViewContact.setText("");
                    autoCompleteTextViewContact.setHint(getActivity().getString(R.string.black_board_add_item_contact));
                    imageButtonContact.setImageDrawable(getActivity().getDrawable(R.drawable.ic_arrow_down));
                }
            }
        });

        return view;
    }

    public void openDatePicker(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditTextDate();
            }

        };

        new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateEditTextDate(){
        String myFormat = "dd.mm.yyyy hh:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(calendar.getTime()));
    }
}
