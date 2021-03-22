package com.example.itunes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itunes.R
import com.example.itunes.model.Track

class ListRecyclerAdapter(val context: Context,val itemList: ArrayList<Track>): RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_single_row,parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val track = itemList[position]
        holder.txtArtistName.text = track.artistName
        holder.txtArtistId.text = track.artistId.toString()
        holder.txtTrackName.text = track.trackName
        holder.txtTrackId.text = track.trackId.toString()
        holder.primaryGenreName.text = track.primaryGenreName
        val milisec: Int = track.trackTime
        val second: Int = milisec/1000
        val minute: Int = second/60
        val sec: Int = second%60
        val s: String
        if(sec<10)
        {
            s = "0"+sec.toString()
        }
        else
        {
            s = sec.toString()
        }
        val time: String = minute.toString()+":"+s
        holder.trackTime.text = time
    }

    class ListViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtArtistName: TextView = view.findViewById(R.id.txtArtistName)
        val txtArtistId: TextView = view.findViewById(R.id.txtArtistId)
        val txtTrackName: TextView = view.findViewById(R.id.txtTrackName)
        val txtTrackId: TextView = view.findViewById(R.id.txtTrackId)
        val primaryGenreName: TextView = view.findViewById(R.id.txtGenreName)
        val trackTime: TextView = view.findViewById(R.id.txtTrackTime)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }

}