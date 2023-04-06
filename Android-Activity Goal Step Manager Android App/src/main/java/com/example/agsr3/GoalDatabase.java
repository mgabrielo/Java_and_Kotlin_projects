package com.example.agsr3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GoalModal.class}, version = 6, exportSchema = false)
public abstract class GoalDatabase extends RoomDatabase {

    private static GoalDatabase goalDatabase;
    private static String DATABASE_NAME = "GoalApps";

    public synchronized static GoalDatabase getInstance(Context context){
        if(goalDatabase == null){
            goalDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    GoalDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return goalDatabase;
    }

    public abstract DaoAccess daoAccess();
}
