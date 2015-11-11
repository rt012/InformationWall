package wipraktikum.informationwallandroidapp.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Remi on 11.11.2015.
 */
public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.e("","onActivityCreated:" + activity.getLocalClassName());
    }

    public void onActivityDestroyed(Activity activity) {
        Log.e("", "onActivityDestroyed:" + activity.getLocalClassName());
    }

    public void onActivityPaused(Activity activity) {
        Log.e("","onActivityPaused:" + activity.getLocalClassName());
    }

    public void onActivityResumed(Activity activity) {
        Log.e("","onActivityResumed:" + activity.getLocalClassName());
    }

    public void onActivitySaveInstanceState(Activity activity,
                                            Bundle outState) {
        Log.e("","onActivitySaveInstanceState:" + activity.getLocalClassName());
    }

    public void onActivityStarted(Activity activity) {
        Log.e("","onActivityStarted:" + activity.getLocalClassName());
    }

    public void onActivityStopped(Activity activity) {
        Log.e("","onActivityStopped:" + activity.getLocalClassName());
    }
}