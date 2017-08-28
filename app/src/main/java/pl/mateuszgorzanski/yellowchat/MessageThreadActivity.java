package pl.mateuszgorzanski.yellowchat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MessageThreadActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private AsyncTask<Void, Void, Void> getMessages;

    ArrayList<HashMap<String, String>> messagesList;

    String user_id;
    String from_user_id;
    LinearLayout messages_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messagesList = new ArrayList<>();

        messages_list = (LinearLayout) findViewById(R.id.messages_list);

        from_user_id = getIntent().getStringExtra("EXTRA_FROM_USER_ID");

        getMessages = new getMessages();
        getMessages.execute();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        getMessages.cancel(true);
    }

    public String getOption(String[] projection, String selection, String[] selectionArg, String sortOrder) {
        OptionsDBHelper mDbHelper = new OptionsDBHelper(getApplicationContext());
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
        @Override
        protected Void doInBackground(Void... arg0) {

            String[] projection = { OptionsDBHelper.COLUMN_NAME_VALUE };
            String selection = OptionsDBHelper.COLUMN_NAME_OPTION + " = ?";
            String[] selectionArgs = { "user_id" };
            String sortOrder = OptionsDBHelper.COLUMN_NAME_VALUE + " DESC";

            user_id = getOption(projection, selection, selectionArgs, sortOrder);

            HttpHandler sh = new HttpHandler();
            String url = "http://yellow-chat.7m.pl/get_data.php?thread_messages&first_user_id=" + user_id + "&second_user_id=" + from_user_id;
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
                        String message_body = c.getString("message");

                        HashMap<String, String> message = new HashMap<>();

                        message.put("id", id);
                        message.put("from_user_id", from_user_id);
                        message.put("from_user_login", from_user_login);
                        message.put("from_user_profile_image", from_user_profile_image);
                        message.put("to_user_id", to_user_id);
                        message.put("message", message_body);

                        messagesList.add(message);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
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
            for(HashMap<String, String> map : messagesList) {
                if(user_id.equals(map.get("from_user_id"))) {
                    View single_message_layout = getLayoutInflater().inflate(R.layout.message_thread_row_from_user, messages_list, false);
                    TextView message = (TextView) single_message_layout.findViewById(R.id.message_from_user);
                    message.setText(map.get("message"));
                    messages_list.addView(single_message_layout);
                    Log.e(TAG, "Message from me, id: " + map.get("id"));
                } else if(user_id.equals(map.get("to_user_id"))) {
                    View single_message_layout = getLayoutInflater().inflate(R.layout.message_thread_row_to_user, messages_list, false);
                    TextView message = (TextView) single_message_layout.findViewById(R.id.message_to_user);
                    message.setText(map.get("message"));
                    ImageView profileImage = (ImageView) single_message_layout.findViewById(R.id.messageProfileImage);
                    if (map.get("from_user_profile_image") != "null") {
                        new ProfileImageDownloadTask(profileImage)
                                .execute((String) map.get("user_from_profile_image"));
                    }
                    messages_list.addView(single_message_layout);
                    Log.e(TAG, "Message to me, id: " + map.get("id"));
                }
            }
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
