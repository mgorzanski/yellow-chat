package pl.mateuszgorzanski.yellowchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class MessagesListFragment extends Fragment {
    public MessagesListFragment() {

    }

    // Array of strings for ListView Title
    String[] messageRowViewTitle = new String[]{
            "Mateusz Górzański", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] messageRowViewImage = new int[]{
            R.drawable.ic_profile, R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account,
            R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account, R.drawable.ic_account,
    };

    String[] messageRowViewShortDescription = new String[]{
            "Wiadomość testowa", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };

    String[] messageRowViewDate = new String[]{
            "5 mins", "5 mins", "5 mins", "5 mins", "5 mins", "5 mins", "5 mins", "5 mins",
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanveState) {
        View fragmentMessagesListView = inflater.inflate(R.layout.fragment_messages_list, container, false);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("message_title", messageRowViewTitle[i]);
            hm.put("message_description", messageRowViewShortDescription[i]);
            hm.put("message_image", Integer.toString(messageRowViewImage[i]));
            hm.put("message_date", messageRowViewDate[i]);
            aList.add(hm);
        }

        String[] from = {"message_image", "message_title", "message_description", "message_date"};
        int[] to = {R.id.message_row_view_image, R.id.message_row_view_title, R.id.message_row_view_short_description, R.id.message_row_view_date};

        SimpleAdapter simpleAdapter = new SimpleAdapter(fragmentMessagesListView.getContext(), aList, R.layout.message_row_view, from, to);
        ListView androidListView = (ListView) fragmentMessagesListView.findViewById(R.id.messagesListView);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageThreadActivity.class);
                startActivity(intent);
            }
        });


        return fragmentMessagesListView;
    }
}