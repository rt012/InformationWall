package wipraktikum.informationwallandroidapp.BlackBoard.CustomView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import wipraktikum.informationwallandroidapp.BusinessObject.Contact.Contact;
import wipraktikum.informationwallandroidapp.R;
import wipraktikum.informationwallandroidapp.Utils.StringHelper;

/**
 * Created by Remi on 28.10.2015.
 */
public class BlackBoardContactView extends TableLayout {
    public BlackBoardContactView(final Context context, final Contact blackBoardContact) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_blackboard_ex_lv_child_contact, this);

        if (blackBoardContact.isEmpty()){
            findViewById(R.id.layout_container).setVisibility(View.GONE);
            return;
        }

        //Contact Information
        TextView txtContactCompany = (TextView) findViewById(R.id.tv_black_board_contact_company);
        ImageView ivContactCompany = (ImageView) findViewById(R.id.iv_black_board_contact_company);
        TableRow trContactCompany = (TableRow) findViewById(R.id.tr_black_board_contact_company);
        if (!StringHelper.isStringNullOrEmpty(blackBoardContact.getCompany())) {
            txtContactCompany.setText(blackBoardContact.getCompany());
            ivContactCompany.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_company));
        }else{
            trContactCompany.setVisibility(View.GONE);
        }

        TextView txtContactFullName= (TextView) findViewById(R.id.tv_black_board_contact_full_name);
        ImageView ivContactFullName = (ImageView) findViewById(R.id.iv_black_board_contact_full_name);
        TableRow trContactFullName= (TableRow) findViewById(R.id.tr_black_board_contact_full_name);
        if (!StringHelper.isStringNullOrEmpty(blackBoardContact.getFullName())) {
            txtContactFullName.setText(blackBoardContact.getFullName());
            ivContactFullName.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_name));
        }else{
            trContactFullName.setVisibility(View.GONE);
        }

        TextView txtContactEmail = (TextView) findViewById(R.id.tv_black_board_contact_email);
        ImageView ivContactEmail = (ImageView) findViewById(R.id.iv_black_board_contact_email);
        TableRow trContactEmail = (TableRow) findViewById(R.id.tr_black_board_contact_email);
        if (!StringHelper.isStringNullOrEmpty(blackBoardContact.getEMailAddress())) {
            txtContactEmail.setText(blackBoardContact.getEMailAddress());
            ivContactEmail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_email));
            trContactEmail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("message/rfc822");
                    intent.setData(Uri.parse("mailto:" + blackBoardContact.getEMailAddress()));
                    Intent mailer = Intent.createChooser(intent, null);
                    context.startActivity(mailer);
                }
            });
        }else{
            trContactEmail.setVisibility(View.GONE);
        }

        TextView txtContactTel= (TextView) findViewById(R.id.tv_black_board_contact_tel);
        ImageView ivContactTel = (ImageView)findViewById(R.id.iv_black_board_contact_tel);
        TableRow trContactTel = (TableRow) findViewById(R.id.tr_black_board_contact_tel);
        if (!StringHelper.isStringNullOrEmpty(blackBoardContact.getTelephone())) {
            txtContactTel.setText(blackBoardContact.getTelephone());
            ivContactTel.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_telephone));
            trContactTel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + blackBoardContact.getTelephone().trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    context.startActivity(intent);
                }
            });

        }else{
            trContactTel.setVisibility(View.GONE);
        }

        TextView txtContactFullAddress = (TextView) findViewById(R.id.tv_black_board_contact_address);
        ImageView ivContactFullAddress = (ImageView) findViewById(R.id.iv_black_board_contact_address);
        TableRow trContactFullAddress = (TableRow) findViewById(R.id.tr_black_board_contact_address);
        if (blackBoardContact.getContactAddress() != null && !StringHelper.isStringNullOrEmpty(blackBoardContact.getContactAddress().getFullAddress())) {
            txtContactFullAddress.setText(blackBoardContact.getContactAddress().getFullAddress());
            ivContactFullAddress.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_address));
        }else{
            trContactFullAddress.setVisibility(View.GONE);
        }
    }
}
