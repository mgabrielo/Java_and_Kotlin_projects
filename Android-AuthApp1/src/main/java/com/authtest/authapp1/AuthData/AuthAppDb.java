package com.authtest.authapp1.AuthData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {AuthAppModal.class}, version = 7, exportSchema = false)
public abstract class AuthAppDb extends RoomDatabase {

    private static AuthAppDb auth_app_Database;
    private static String auth_app_db_NAME = "AuthApps";

    public synchronized static AuthAppDb getInstance(Context context){
        if(auth_app_Database == null){
            auth_app_Database = Room.databaseBuilder(context.getApplicationContext(),
                    AuthAppDb.class, auth_app_db_NAME)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return auth_app_Database;
    }

    public abstract AuthAppDAO authAppDAO();

}
