package wipraktikum.informationwallandroidapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Eric Schmidt on 04.01.2016.
 */
public class ProgressDialogHelper {

    private ProgressDialog progressDialog = null;

    public ProgressDialogHelper(Context context){
        progressDialog = new ProgressDialog(context);
    }

    public void show(String title, String message){
        if (!progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void hide(){
        if (progressDialog.isShowing()) progressDialog.hide();
    }
}
