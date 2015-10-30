package wipraktikum.informationwallandroidapp.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class NotificationHelper {
    private static NotificationHelper instance = null;
    private Context mContext =null;
    private static int notifyID = 0;

    private NotificationHelper(){
        mContext = InfoWallApplication.getInstance();
    }

    public static NotificationHelper getInstance(){
        if (instance == null){
            instance = new NotificationHelper();
        }
        return instance;
    }

    public int startNotification(String notificationText){
        notifyID++;

        NotificationManager mNotifyManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(mContext.getString(R.string.notification_upload))
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_upload);
        builder.setProgress(0, 0, true);
        mNotifyManager.notify(notifyID, builder.build());

        return notifyID;
    }

    public void closeNotification(int notifyID){
        NotificationManager mNotifyManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.cancel(notifyID);
    }

}
