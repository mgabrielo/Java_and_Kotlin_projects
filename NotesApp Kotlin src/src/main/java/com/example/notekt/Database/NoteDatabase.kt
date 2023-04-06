package com.example.notekt.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notekt.Modals.Note
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = arrayOf(Note::class) , version = 8, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {

       @Volatile
       private  var INSTANCE :  NoteDatabase? = null

        fun getDatabase(context: Context) : NoteDatabase{

            return INSTANCE ?: kotlin.synchronized(this ){

                val instance =  Room.databaseBuilder(context.applicationContext
                , NoteDatabase::class.java, "database_name" ).build()

                INSTANCE =instance

                instance
            }
        }
    }
}