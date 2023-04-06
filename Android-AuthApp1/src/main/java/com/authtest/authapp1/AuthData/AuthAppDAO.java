package com.authtest.authapp1.AuthData;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AuthAppDAO {

    @Insert(onConflict = REPLACE)
    void insert(AuthAppModal...authAppModal);

    @Query("SELECT * FROM auth_data_table ORDER BY authEmail DESC")
    List<AuthAppModal> getAll();

    @Query("SELECT authPhone FROM auth_data_table WHERE authEmail = :AuthEmail")
    String getUserPhone(String AuthEmail);

    @Query("UPDATE auth_data_table SET  authPhone = :AuthPhone WHERE authEmail = :AuthEmail")
    void update(String AuthPhone, String AuthEmail);

    @Delete
    void delete(AuthAppModal authAppModal);


}
