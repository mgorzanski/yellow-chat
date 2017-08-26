package pl.mateuszgorzanski.yellowchat;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private Fragment defaultFragment;
    private FragmentManager fragmentManager;
    private int currentFragment;
    private boolean fragmentChanged;

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
            finish();
        }


        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        //load default layout
        defaultFragment = new MessagesListFragment();
        currentFragment = 1;
        //final FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.main_container, defaultFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.main_container, defaultFragment).commit();

        //load different layouts on bottom navigation buttons
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_messages:
                        if(currentFragment != 1) {
                            fragment = new MessagesListFragment();
                            currentFragment = 1;
                            fragmentChanged = true;
                        }
                        break;
                    case R.id.navigation_contacts:
                        if(currentFragment != 2) {
                            fragment = new ContactsListFragment();
                            currentFragment = 2;
                            fragmentChanged = true;
                        }
                        break;
                    case R.id.navigation_settings:
                        if(currentFragment != 3) {
                            fragment = new SettingsListFragment();
                            currentFragment = 3;
                            fragmentChanged = true;
                        }
                        break;
                }
                //final FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.replace(R.id.main_container, fragment).commit();
                if(fragmentChanged) {
                    getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                }
                return true;
            }
        });
    }

}
