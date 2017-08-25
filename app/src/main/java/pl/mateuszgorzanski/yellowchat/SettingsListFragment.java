package pl.mateuszgorzanski.yellowchat;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class SettingsListFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
    }
}