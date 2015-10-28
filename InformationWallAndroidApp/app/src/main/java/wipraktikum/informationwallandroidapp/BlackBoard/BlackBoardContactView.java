package wipraktikum.informationwallandroidapp.BlackBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.Database.BusinessObject.DBContact;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 28.10.2015.
 */
public class BlackBoardContactView extends TableLayout {
    public BlackBoardContactView(Context context, DBContact blackBoardContact) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_black_board_ex_lv_child_contact, this);

        //Contact Information
        TextView txtContactCompany = (TextView) findViewById(R.id.tv_black_board_contact_company);
        txtContactCompany.setText(blackBoardContact.getCompany());
        ImageView ivContactCompany = (ImageView) findViewById(R.id.iv_black_board_contact_company);
        ivContactCompany.setImageDrawable(context.getDrawable(R.drawable.icon_company));

        TextView txtContactFullName= (TextView) findViewById(R.id.tv_black_board_contact_full_name);
        txtContactFullName.setText(blackBoardContact.getFullName());
        ImageView ivContactFullName = (ImageView) findViewById(R.id.iv_black_board_contact_full_name);
        ivContactFullName.setImageDrawable(context.getDrawable(R.drawable.icon_name));

        TextView txtContactEmail = (TextView) findViewById(R.id.tv_black_board_contact_email);
        txtContactEmail.setText(blackBoardContact.getEMailAddress());
        ImageView ivContactEmail = (ImageView) findViewById(R.id.iv_black_board_contact_email);
        ivContactEmail.setImageDrawable(context.getDrawable(R.drawable.icon_email));

        TextView txtContactTel= (TextView) findViewById(R.id.tv_black_board_contact_tel);
        txtContactTel.setText(blackBoardContact.getTelephone());
        ImageView ivContactTel = (ImageView)findViewById(R.id.iv_black_board_contact_tel);
        ivContactTel.setImageDrawable(context.getDrawable(R.drawable.icon_telephone));

        TextView txtContactFullAddress = (TextView) findViewById(R.id.tv_black_board_contact_address);
        txtContactFullAddress.setText(blackBoardContact.getContactAddress().getFullAddress());
        ImageView ivContactFullAddress = (ImageView) findViewById(R.id.iv_black_board_contact_address);
        ivContactFullAddress.setImageDrawable(context.getDrawable(R.drawable.icon_address));

    }
}
