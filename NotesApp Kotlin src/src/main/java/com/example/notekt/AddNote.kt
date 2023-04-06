package com.example.notekt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notekt.Modals.Note
import com.example.notekt.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*


class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var  note : Note

    private lateinit var  old_note : Note

    var is_update =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.edtTitle.setText(old_note.title)
            binding.edtNote.setText(old_note.note)
            is_update =true
        }catch (e : Exception){
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener{
            var title = binding.edtTitle.text.toString()
            var noted  = binding.edtNote.text.toString()

            if(title.isNotEmpty() || noted.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d  MMM  yyyy HH:mm a")
                val intent = Intent()

                if(is_update){
                    note  = Note(
                     old_note.id,title, noted, formatter.format(Date())
                    )
                    intent.putExtra("update", note)
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                }else{
                    note  = Note(null, title, noted, formatter.format(Date()))
                    intent.putExtra("create", note)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }else{
                Toast.makeText(this@AddNote, "Please Enter Some Data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }


            binding.imgBackArrow.setOnClickListener{
                val intent = Intent(this@AddNote, MainActivity::class.java)
                startActivity(intent)
            }
    }

}