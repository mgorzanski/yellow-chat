package pl.mateuszgorzanski.yellowchat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://yellow-chat.7m.pl/get_data.php?action=login&user_login=mateusz&user_password=123";
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
            } else {
                Log.e(TAG, "Błąd");
            }
        }
    }


}

