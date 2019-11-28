package com.weedeo.user.databse;

import android.content.ContentValues;

public interface DatabaseContract {

    interface MvpView  {
        void isSuccessfullyInserted(boolean status);
        void isDatabaseSuccessfullyUpdated(boolean status);
        void onDatabaseSuccessfullyDeleted();
    }

    interface Presenter{
        void onInsertDataIntoDatabase(ContentValues contentValues);
        void onUpdateUserData(ContentValues values, String userId);
        void deleteDatabaseValues();
        String getItemFromDatabase(String key);
        boolean isUserLoggedIn();
    }

}
