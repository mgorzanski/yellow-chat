package pl.mateuszgorzanski.yellowchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class ContactsListFragment extends Fragment {
    public ContactsListFragment() {

    }
    // Array of strings for ListView Title
    String[] contactRowViewTitle = new String[]{
            "Mateusz Górzański", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };

    int[] contactRowViewImage = new int[]{
            R.drawable.ic_profile, R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account,
            R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanveState) {
        View fragmentContactsListView = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("contact_title", contactRowViewTitle[i]);
            hm.put("contact_image", Integer.toString(contactRowViewImage[i]));
            aList.add(hm);
        }

        String[] from = {"contact_image", "contact_title"};
        int[] to = {R.id.contact_row_view_image, R.id.contact_row_view_title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(fragmentContactsListView.getContext(), aList, R.layout.contact_row_view, from, to);
        ListView androidListView = (ListView) fragmentContactsListView.findViewById(R.id.contactsListView);
        androidListView.setAdapter(simpleAdapter);

        return fragmentContactsListView;
    }
}