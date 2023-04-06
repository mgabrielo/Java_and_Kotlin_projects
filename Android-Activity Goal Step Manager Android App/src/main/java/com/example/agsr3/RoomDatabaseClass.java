package com.example.agsr3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {StatusDB.class},version=4,exportSchema = false)
public abstract class RoomDatabaseClass extends RoomDatabase {

    private static RoomDatabaseClass roomDatabaseClass;
    private static String DATABASE_NAME = "StatusApps";

    public synchronized static RoomDatabaseClass getInstance(Context context){
        if(roomDatabaseClass == null){
            roomDatabaseClass = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabaseClass.class, DATABASE_NAME)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return roomDatabaseClass;
    }


    public abstract StatusDao statusDao();
}