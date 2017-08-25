package pl.mateuszgorzanski.yellowchat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private Fragment defaultFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*OptionsDBHelper mDbHelper = new OptionsDBHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(OptionsDBHelper.COLUMN_NAME_ID, 1);
        values.put(OptionsDBHelper.COLUMN_NAME_OPTION, "app_version");
        values.put(OptionsDBHelper.COLUMN_NAME_VALUE, "1.0");

        long newRowId = db.insert(OptionsDBHelper.TABLE_NAME, null, values);

        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        String[] projection = {
                OptionsDBHelper.COLUMN_NAME_ID,
                OptionsDBHelper.COLUMN_NAME_OPTION,
                OptionsDBHelper.COLUMN_NAME_VALUE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = OptionsDBHelper.COLUMN_NAME_OPTION + " = ?";
        String[] selectionArgs = { "app_version" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                OptionsDBHelper.COLUMN_NAME_VALUE + " DESC";

        Cursor cursor = db.query(
                OptionsDBHelper.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        String app_version = "";

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            app_version = cursor.getString(cursor.getColumnIndex("value"));
            Log.e(TAG, app_version);
        }*/

        OptionsDBHelper mDbHelper = new OptionsDBHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                OptionsDBHelper.COLUMN_NAME_ID,
                OptionsDBHelper.COLUMN_NAME_OPTION,
                OptionsDBHelper.COLUMN_NAME_VALUE
        };

        String selection = OptionsDBHelper.COLUMN_NAME_OPTION + " = ?";
        String[] selectionArgs = { "user_id" };

        String sortOrder =
                OptionsDBHelper.COLUMN_NAME_VALUE + " DESC";

        Cursor cursor = db.query(
                OptionsDBHelper.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if(cursor.getCount() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        //load default layout
        defaultFragment = new MessagesListFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, defaultFragment).commit();

        //load different layouts on bottom navigation buttons
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_messages:
                        fragment = new MessagesListFragment();
                        break;
                    case R.id.navigation_contacts:
                        fragment = new ContactsListFragment();
                        break;
                    case R.id.navigation_settings:
                        fragment = new SettingsListFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }

}
