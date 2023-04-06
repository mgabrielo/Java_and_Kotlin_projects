package com.example.agsr3;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface DaoAccess {

 @Insert(onConflict = REPLACE)
 void insert(GoalModal goals);

 @Query("SELECT * FROM goal_tables ORDER BY id DESC")
 List<GoalModal> getAll();

 @Query("UPDATE goal_tables SET ActSelect = :ActSelect, StepSelect = :StepSelect WHERE id = :id")
 void update(int id, String ActSelect, String StepSelect);

 @Delete
 void delete(GoalModal goals);

 @Query("UPDATE goal_tables SET pinned = :pin  WHERE id = :id")
 void pin(int id , boolean pin);

}
