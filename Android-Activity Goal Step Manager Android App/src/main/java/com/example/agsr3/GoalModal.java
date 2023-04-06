package com.example.agsr3;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "goal_tables")
public class GoalModal implements Serializable{

    @PrimaryKey(autoGenerate = true)
    int id = 0;

    @ColumnInfo(name = "ActSelect")
    String ActSelect ;

    @ColumnInfo(name = "StepSelect")
    String StepSelect ;

    @ColumnInfo(name = "DateSelects")
    String DateSelects ;


    @ColumnInfo(name = "pinned")
   boolean pinned = false ;

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActSelect() {
        return ActSelect;
    }

    public void setActSelect(String actSelect) {
        ActSelect = actSelect;
    }

    public String getStepSelect() {
        return StepSelect;
    }

    public void setStepSelect(String stepSelect) {
        StepSelect = stepSelect;
    }

    public String getDateSelects() {
        return DateSelects;
    }

    public void setDateSelects(String dateSelects) {
        DateSelects = dateSelects;
    }
}
