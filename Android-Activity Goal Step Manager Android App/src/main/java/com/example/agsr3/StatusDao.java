package com.example.agsr3;


import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert(onConflict = REPLACE)
    void insert(StatusDB statusDB);

    @Query("SELECT * FROM status_tables ORDER BY uid DESC")
    List<StatusDB> getAllStats();

    @Query("UPDATE status_tables SET ActSect = :ActSect, StepSect = :StepSect, ExtraSects=:ExtraSects, DateSects =:DateSects /*, ProgressbarFill =:ProgressbarFill, ProgressbarText =:ProgressbarText */ WHERE uid = :id")
    void update(int id, String ActSect, String StepSect ,String ExtraSects, String DateSects /*, int ProgressbarFill, String ProgressbarText*/);

    @Delete
    void delete(StatusDB statusDB);

    @Query("UPDATE status_tables SET ProgressbarText = :ProgressbarText  WHERE uid = :id")
    void pin(int id, String ProgressbarText);

}
