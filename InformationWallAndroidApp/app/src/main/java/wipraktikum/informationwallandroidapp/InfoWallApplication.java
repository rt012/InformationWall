package wipraktikum.informationwallandroidapp;

import android.app.Application;

import wipraktikum.informationwallandroidapp.Database.InformationWallORMHelper;

/**
 * Created by Remi on 26.10.2015.
 */
public class InfoWallApplication extends Application {
    private static InfoWallApplication instance;
    private InformationWallORMHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static InfoWallApplication getInstance() {
        return instance;
    }

    public InformationWallORMHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = new InformationWallORMHelper(this);
        }
        return databaseHelper;
    }
}
