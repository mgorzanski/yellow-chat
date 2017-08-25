package pl.mateuszgorzanski.yellowchat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class ContactsListFragment extends Fragment {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

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

        //List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        contactList = new ArrayList<>();
        lv = (ListView) fragmentContactsListView.findViewById(R.id.contactsListView);

        new getContacts().execute();

        /*for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("contact_title", contactRowViewTitle[i]);
            hm.put("contact_image", Integer.toString(contactRowViewImage[i]));
            aList.add(hm);
        }*/

        //String[] from = {"contact_image", "contact_title"};
        //int[] to = {R.id.contact_row_view_image, R.id.contact_row_view_title};

        //SimpleAdapter simpleAdapter = new SimpleAdapter(fragmentContactsListView.getContext(), aList, R.layout.contact_row_view, from, to);
        //ListView androidListView = (ListView) fragmentContactsListView.findViewById(R.id.contactsListView);
        //androidListView.setAdapter(simpleAdapter);

        return fragmentContactsListView;
    }

    private class getContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Trwa pobieranie danych",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://api.myjson.com/bins/lug31";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String login = c.getString("name");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("id", id);
                        contact.put("login", login);

                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getActivity(), contactList, R.layout.contact_row_view, new String[]{ "login" },
                    new int[]{R.id.contact_row_view_title});
            lv.setAdapter(adapter);
        }
    }


}