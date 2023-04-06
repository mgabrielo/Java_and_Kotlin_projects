package com.example.notekt

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notekt.Adapter.NotesAdapter
import com.example.notekt.Database.NoteDatabase
import com.example.notekt.Modals.Note
import com.example.notekt.Modals.NoteViewModal
import com.example.notekt.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(),NotesAdapter.NotesItemClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database :  NoteDatabase
    lateinit var viewModal : NoteViewModal
    lateinit var  adapter :  NotesAdapter
    lateinit var selectednote : Note

    private  val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

                if(result.resultCode == Activity.RESULT_OK){

                    val note = result.data?.getSerializableExtra("update") as? Note

                    if(note != null){
                        viewModal.updateNote(note)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        InitUI()

        viewModal = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModal::class.java)

        viewModal.allnotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = NoteDatabase.getDatabase(this)
    }

    private fun InitUI() {
       binding.recyclerViewed.setHasFixedSize(true)
        binding.recyclerViewed.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)



        binding.recyclerViewed.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("create") as? Note

                if (note != null){
                    viewModal.insertNote(note)
                }
            }

        }

        binding.fbAddNote.setOnClickListener{
            val  intent  =  Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchViewed.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               if(newText != null){
                   adapter.filterList(newText)
               }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
       val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onDeleteItemClicked(note: Note) {
       selectednote = note
        val note_deletes = findViewById<ImageView>(R.id.note_delete)
        popUpDisplay(note_deletes)
    }

    private fun popUpDisplay(note_del: ImageView) {
        val popUp = PopupMenu(this, note_del)
        popUp.setOnMenuItemClickListener ( this@MainActivity )
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_ok){

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.Deleting_Note)
            //set message for alert dialog
            builder.setMessage(R.string.Do_You_Want_to_Delete_Note)
            builder.setIcon(AppCompatResources.getDrawable(this,R.drawable.ic_baseline_warning_24 ))

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                viewModal.deleteNote(selectednote)
            }

            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                dialogInterface.dismiss()
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

            return true
        }

        if(item?.itemId == R.id.delete_cancel){
            return false
        }
        return false
    }


}


