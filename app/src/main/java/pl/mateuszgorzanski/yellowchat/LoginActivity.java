package pl.mateuszgorzanski.yellowchat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private int user_id;
    private String user_login;
    private String user_password;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View v) {
        new getResponse().execute();
    }

    private class getResponse extends AsyncTask<Void, Void, Void> {
        EditText loginForm = (EditText) findViewById(R.id.login);
        EditText passwordForm = (EditText) findViewById(R.id.password);
        String login = loginForm.getText().toString();
        String password = passwordForm.getText().toString();

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://yellow-chat.7m.pl/get_data.php?action=login&user_login=" + login + "&user_password=" + password;
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    response = jsonObj.getString("response");
                    if(jsonObj.has("user_id")) {
                        user_id = jsonObj.getInt("user_id");
                    }
                    if(jsonObj.has("user_login")) {
                        user_login = jsonObj.getString("user_login");
                        user_password = jsonObj.getString("user_password");
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(response.equals("success")) {
                Log.e(TAG, "Zalogowano");

                ContentValues values1 = new ContentValues();
                values1.put(OptionsDBHelper.COLUMN_NAME_OPTION, "user_id");
                values1.put(OptionsDBHelper.COLUMN_NAME_VALUE, user_id);

                ContentValues values2 = new ContentValues();
                values2.put(OptionsDBHelper.COLUMN_NAME_OPTION, "user_login");
                values2.put(OptionsDBHelper.COLUMN_NAME_VALUE, user_login);

                ContentValues values3 = new ContentValues();
                values3.put(OptionsDBHelper.COLUMN_NAME_OPTION, "user_password");
                values3.put(OptionsDBHelper.COLUMN_NAME_VALUE, user_password);

                insertOption(values1);
                insertOption(values2);
                insertOption(values3);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e(TAG, "Błąd");
            }
        }
    }

    public void insertOption(ContentValues values) {
        OptionsDBHelper mDbHelper = new OptionsDBHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(OptionsDBHelper.TABLE_NAME, null, values);
    }


}

