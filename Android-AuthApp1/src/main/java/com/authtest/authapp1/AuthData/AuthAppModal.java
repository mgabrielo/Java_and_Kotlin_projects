package com.authtest.authapp1.AuthData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "auth_data_table", primaryKeys = {"AuthEmail"})
public class AuthAppModal{


    @NonNull
    @ColumnInfo(name = "AuthEmail")
    public String authEmail ="0";


    @ColumnInfo(name = "AuthPhone")
    public String authPhone;

  /*  public AuthAppModal(String authEmail, String authPassword) {
        this.authEmail = authEmail;
        this.authPassword = authPassword;
    }
*/

    public AuthAppModal(@NonNull String authEmail, String authPhone) {
        this.authEmail = authEmail;
        this.authPhone = authPhone;
    }

    @NonNull
    public String getAuthEmail() {
        return authEmail;
    }

    public void setAuthEmail(@NonNull String authEmail) {
        this.authEmail = authEmail;
    }

    public String getAuthPhone() {
        return authPhone;
    }

    public void setAuthPhone(String authPhone) {
        this.authPhone = authPhone;
    }
}
