package wipraktikum.informationwallandroidapp.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Remi on 08.11.2015.
 */
public class GcmListener extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        System.out.print(data);
    }
}
