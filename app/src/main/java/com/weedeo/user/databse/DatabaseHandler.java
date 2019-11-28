package com.weedeo.user.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.weedeo.user.Utils.Constants.DATABASE_NAME;
import static com.weedeo.user.Utils.Constants.DATABASE_VERSION;
import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_DEVICE_ID;
import static com.weedeo.user.Utils.Constants.KEY_FCM_UID;
import static com.weedeo.user.Utils.Constants.KEY_USER_EMAIL;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.KEY_USER_IMAGE;
import static com.weedeo.user.Utils.Constants.KEY_USER_NAME;
import static com.weedeo.user.Utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.weedeo.user.Utils.Constants.TABLE_USER;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method for creating table.
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " TEXT," + KEY_FCM_UID + " TEXT,"
                + KEY_USER_NAME + " TEXT," + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_IMAGE + " TEXT," + KEY_USER_PHONE_NUMBER + " TEXT,"
                + KEY_DEVICE_ID + " TEXT," + KEY_ACCESS_TOKEN + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);

    }
}
