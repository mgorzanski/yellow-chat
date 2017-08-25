package pl.mateuszgorzanski.yellowchat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class SettingsListFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        Preference settingsLogout = (Preference) findPreference("logout");
        settingsLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                deleteFromOptions(new String[]{"user_id"});
                deleteFromOptions(new String[]{"user_login"});
                deleteFromOptions(new String[]{"user_password"});


                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                return false;
            }
        });
    }

    public void deleteFromOptions(String[] selectionArg) {
        OptionsDBHelper mDbHelper = new OptionsDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = OptionsDBHelper.COLUMN_NAME_OPTION + " LIKE ?";
        db.delete(OptionsDBHelper.TABLE_NAME, selection, selectionArg);
    }
}