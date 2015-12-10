package wipraktikum.informationwallandroidapp.Utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import wipraktikum.informationwallandroidapp.AppConfig;
import wipraktikum.informationwallandroidapp.InfoWallApplication;
import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Eric Schmidt on 30.10.2015.
 */
public class NotificationHelper {
    private static NotificationHelper instance = null;

    private Context mContext =null;
    private static int notifyID = 0;
    private OnNotificationReceiveListener mOnNotificationReceiveListener = null;

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

    public void showNotificationMessage(Intent intent, String message, String title) {
        if (isAppIsInBackground(mContext)) {
            // notification icon
            int largeIcon = R.drawable.icon_main;

            int smallIcon = R.drawable.icon_main;

            int mNotificationId = AppConfig.NOTIFICATION_ID;

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    mContext);
            Notification notification = mBuilder.setSmallIcon(smallIcon).setTicker("").setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(inboxStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon))
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);
        } else {
            if (mOnNotificationReceiveListener != null) {
                mOnNotificationReceiveListener.onNotificationReceive();
            }
        }
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void setOnNotificationReceiveListener(OnNotificationReceiveListener onNotificationReceiveListener){
        mOnNotificationReceiveListener = onNotificationReceiveListener;
    }

    public interface OnNotificationReceiveListener{
        public void onNotificationReceive();
    }

}
