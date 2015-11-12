package wipraktikum.informationwallandroidapp.Preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import wipraktikum.informationwallandroidapp.R;

/**
 * Created by Remi on 12.11.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_preferences);
    }}
