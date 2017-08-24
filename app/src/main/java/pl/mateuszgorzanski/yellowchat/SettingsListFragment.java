package pl.mateuszgorzanski.yellowchat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class SettingsListFragment extends Fragment {
    public SettingsListFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanveState) {
        return inflater.inflate(R.layout.fragment_settings_list, container, false);
    }
}