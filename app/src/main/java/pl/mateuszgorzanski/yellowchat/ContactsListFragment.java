package pl.mateuszgorzanski.yellowchat;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mateusz on 24.08.2017.
 */

public class ContactsListFragment extends Fragment {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private AsyncTask<Void, Void, Void> getContacts;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanveState) {
        View fragmentContactsListView = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        //List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        contactList = new ArrayList<>();
        lv = (ListView) fragmentContactsListView.findViewById(R.id.contactsListView);

        getContacts = new getContacts();
        getContacts.execute();

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

    @Override
    public void onPause() {
        super.onPause();
        getContacts.cancel(true);
    }

    private class getContacts extends AsyncTask<Void, Void, Void> {
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Trwa pobieranie danych",Toast.LENGTH_LONG).show();
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://yellow-chat.7m.pl/get_data.php?users";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("users");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String login = c.getString("login");
                        String profile_image = c.getString("profile_image");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("id", id);
                        contact.put("login", login);
                        contact.put("profile_image", profile_image);

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
            ListAdapter adapter = new MySimpleAdapter(getActivity(), contactList, R.layout.contact_row_view, new String[]{},
                    new int[]{});
            lv.setAdapter(adapter);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled();
        }
    }

    public class MySimpleAdapter extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource, String[] from,
                               int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.contact_row_view, null);

            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

            TextView login = (TextView) vi.findViewById(R.id.contact_row_view_title);
            String loginText  = (String) data.get("login");
            login.setText(loginText);

            if (data.get("profile_image") != "null") {
                new ProfileImageDownloadTask((ImageView) vi.findViewById(R.id.contact_row_view_image))
                        .execute((String) data.get("profile_image"));
            }

            return vi;
        }

    }

}