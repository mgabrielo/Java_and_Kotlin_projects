package com.example.notekt.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notekt.Modals.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note : Note)

    @Delete
    suspend fun delete(note : Note)

    @Update
    suspend fun update(note : Note)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes() : LiveData<List<Note>>

   // @Query("UPDATE notes_table Set title =:title, note =:note WHERE id =:id ")


}