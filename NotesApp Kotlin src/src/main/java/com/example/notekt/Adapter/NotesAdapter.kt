package com.example.notekt.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notekt.Modals.Note
import com.example.notekt.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class NotesAdapter (private  val context : Context, val listener : NotesItemClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        holder.note_tv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.title.isSelected = true
        holder.date.isSelected = true

        holder.notes_card.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.note_del.setOnClickListener{
            listener.onDeleteItemClicked(notesList[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)


        notesList.clear()
        notesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search :String){
        notesList.clear()

        for (item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase())==true
                || item.note?.lowercase()?.contains(search.lowercase()) ==  true){
                notesList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    fun randomColor() : Int {
        val list =  ArrayList<Int>()
        list.add(R.color.Note_Color_1)
        list.add(R.color.Note_Color_2)
        list.add(R.color.Note_Color_3)
        list.add(R.color.Note_Color_4)
        list.add(R.color.Note_Color_5)
        list.add(R.color.Note_Color_6)
        list.add(R.color.Note_Color_7)

        val seed  = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val notes_layout =  itemView.findViewById<RelativeLayout>(R.id.rel_lay)
        val notes_card = itemView.findViewById<CardView>(R.id.card_v)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val note_del = itemView.findViewById<ImageView>(R.id.note_delete)

    }


    interface NotesItemClickListener{

        fun onItemClicked(note: Note)
        fun onDeleteItemClicked(note: Note)
    }
}