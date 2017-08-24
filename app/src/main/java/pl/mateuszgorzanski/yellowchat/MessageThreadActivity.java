package pl.mateuszgorzanski.yellowchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MessageThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
