package com.example.agsr3;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "status_tables")
public class StatusDB implements Serializable {


    @PrimaryKey(autoGenerate = true)
    int uid = 0;

    @ColumnInfo(name = "ActSect")
    String ActSect = "" ;

    @ColumnInfo(name = "StepSect")
    String StepSect = "" ;

   @ColumnInfo(name = "DateSects")
    String DateSects ;

    @ColumnInfo(name = "ExtraSects")
    String ExtraSects ;

    @ColumnInfo(name = "pins")
    boolean pins = false ;

   /* @ColumnInfo(name = "ProgressbarFill")
    int ProgressbarFill ;*/

    @ColumnInfo(name = "ProgressbarText")
    String ProgressbarText = "0";

    public String getExtraSects() {
        return ExtraSects;
    }

    public void setExtraSects(String extraSects) {
        ExtraSects = extraSects;
    }

    public boolean isPins() {
        return pins;
    }

    public void setPins(boolean pins) {
        this.pins = pins;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getActSect() {
        return ActSect;
    }

    public void setActSect(String actSect) {
        ActSect = actSect;
    }

    public String getStepSect() {
        return StepSect;
    }

    public void setStepSect(String stepSect) {
        StepSect = stepSect;
    }

     public String getDateSects() {
        return DateSects;
    }

    public void setDateSects(String dateSects) {
        DateSects = dateSects;
    }

    /*
   public int getProgressbarFill() {
        return ProgressbarFill;
    }

    public void setProgressbarFill(int progressbarFill) {
        ProgressbarFill = progressbarFill;
    }
*/
    public String getProgressbarText() {
        return ProgressbarText;
    }

    public void setProgressbarText(String progressbarText) {
        ProgressbarText = progressbarText;
    }
}
