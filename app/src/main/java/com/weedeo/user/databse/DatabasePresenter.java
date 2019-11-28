package com.weedeo.user.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.TABLE_USER;

public class DatabasePresenter implements DatabaseContract.Presenter {

    private DatabaseContract.MvpView mvpView;
    private SQLiteDatabase database;
    private Context mContext;

    public DatabasePresenter(DatabaseContract.MvpView view,Context context){
        this.mContext = context;
        mvpView = view;
        database = new DatabaseHandler(mContext).getWritableDatabase();
    }

    @Override
    public void onInsertDataIntoDatabase(ContentValues contentValues) {
        new Thread(() -> {
            try {
                Log.e("login","insertion started");
                database.insert(TABLE_USER,null,contentValues);
                Log.e("login","insertion completed");
                mvpView.isSuccessfullyInserted(true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Insertion Error : ", e.getMessage());
                mvpView.isSuccessfullyInserted(false);
            }
        }).start();

    }

    @Override
    public void onUpdateUserData(ContentValues values, String userId) {
        new Thread(() -> {
            try {
                Log.e("update","insertion started "+userId);
                String[] args = new String[]{userId};
                database.update(TABLE_USER, values, KEY_USER_ID + "=?" , args);
                mvpView.isDatabaseSuccessfullyUpdated(true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Update Error : ", e.getMessage());
                mvpView.isDatabaseSuccessfullyUpdated(false);
            }
        }).start();
    }

    @Override
    public void deleteDatabaseValues() {
        database.execSQL("delete from "+ TABLE_USER);
        mvpView.onDatabaseSuccessfullyDeleted();
    }

    @Override
    public String getItemFromDatabase(String key) {
        try {
            String item = null;
            Cursor cursor= database.query(
                    TABLE_USER, new String[] { key },
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    item = cursor.getString(cursor.getColumnIndex(key));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return item;
        } catch (Exception e) {
            Log.e("DB item fetch error : ",e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isUserLoggedIn() {
        try {
            Cursor cursor= database.query(
                    TABLE_USER, new String[] { KEY_USER_ID },
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex(KEY_USER_ID))!=null){
                        return true;
                    }else{
                        database.execSQL("delete from "+ TABLE_USER);
                        return false;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            return false;

           /* String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_USER_ID;
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor != null&& cursor.getCount()>0)
                cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex(KEY_USER_ID))!=null){
                return true;
            }else {
                database.execSQL("delete from "+ TABLE_USER);
                return false;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
