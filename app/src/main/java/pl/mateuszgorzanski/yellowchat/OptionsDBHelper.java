package pl.mateuszgorzanski.yellowchat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mateusz on 25.08.2017.
 */

public class OptionsDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database";

    public static final String TABLE_NAME = "options";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_OPTION = "name";
    public static final String COLUMN_NAME_VALUE = "value";

    private static final String SQL_CREATE_OPTIONS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_OPTION + " TEXT," +
                    COLUMN_NAME_VALUE + " TEXT)";

    private static final String SQL_DELETE_OPTIONS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public OptionsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_OPTIONS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_OPTIONS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
