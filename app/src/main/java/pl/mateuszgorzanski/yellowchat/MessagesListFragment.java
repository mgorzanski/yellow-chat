package pl.mateuszgorzanski.yellowchat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class MessagesListFragment extends Fragment {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> messagesList;

    // Array of strings for ListView Title
    /*String[] messageRowViewTitle = new String[]{
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
    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanveState) {
        View fragmentMessagesListView = inflater.inflate(R.layout.fragment_messages_list, container, false);
        /*List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

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
        });*/

        messagesList = new ArrayList<>();
        lv = (ListView) fragmentMessagesListView.findViewById(R.id.messagesListView);

        new getMessages().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageThreadActivity.class);
                startActivity(intent);
            }
        });

        return fragmentMessagesListView;
    }

    public String getOption(String[] projection, String selection, String[] selectionArg, String sortOrder) {
        OptionsDBHelper mDbHelper = new OptionsDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                OptionsDBHelper.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArg,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String data = cursor.getString(cursor.getColumnIndex("value"));
            cursor.close();
            return data;
        }
        return null;
    }

    private class getMessages extends AsyncTask<Void, Void, Void> {
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Trwa pobieranie danych",Toast.LENGTH_LONG).show();
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {

            String[] projection = { OptionsDBHelper.COLUMN_NAME_VALUE };
            String selection = OptionsDBHelper.COLUMN_NAME_OPTION + " = ?";
            String[] selectionArgs = { "user_id" };
            String sortOrder = OptionsDBHelper.COLUMN_NAME_VALUE + " DESC";

            String user_id = getOption(projection, selection, selectionArgs, sortOrder);

            HttpHandler sh = new HttpHandler();
            String url = "http://yellow-chat.7m.pl/get_data.php?messages&to_user_id=" + user_id;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray messages = jsonObj.getJSONArray("messages");

                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject c = messages.getJSONObject(i);
                        String id = c.getString("id");
                        String from_user_id = c.getString("from_user_id");
                        String from_user_login = c.getString("from_user_login");
                        String from_user_profile_image = c.getString("from_user_profile_image");
                        String to_user_id = c.getString("to_user_id");
                        String date = c.getString("date");
                        String message_body = c.getString("message");
                        String message_unreaded = c.getString("message_unreaded");

                        HashMap<String, String> message = new HashMap<>();

                        message.put("id", id);
                        message.put("from_user_id", from_user_id);
                        message.put("from_user_login", from_user_login);
                        message.put("from_user_profile_image", from_user_profile_image);
                        message.put("to_user_id", to_user_id);
                        message.put("date", date);
                        message.put("message", message_body);
                        message.put("message_unreaded", message_unreaded);

                        messagesList.add(message);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
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
            ListAdapter adapter = new MySimpleAdapter(getActivity(), messagesList, R.layout.message_row_view, new String[]{},
                    new int[]{});
            lv.setAdapter(adapter);
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
                vi = inflater.inflate(R.layout.message_row_view, null);

            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

            TextView user_login = (TextView) vi.findViewById(R.id.message_row_view_title);
            TextView message = (TextView) vi.findViewById(R.id.message_row_view_short_description);
            TextView date = (TextView) vi.findViewById(R.id.message_row_view_date);

            String user_login_text  = (String) data.get("from_user_login");
            String message_text = (String) data.get("message");
            String date_text = (String) data.get("date");

            user_login.setText(user_login_text);
            message.setText(message_text);
            date.setText(date_text);

            if (data.get("from_user_profile_image") != "null") {
                new ProfileImageDownloadTask((ImageView) vi.findViewById(R.id.message_row_view_image))
                        .execute((String) data.get("from_user_profile_image"));
            }

            return vi;
        }

    }
}