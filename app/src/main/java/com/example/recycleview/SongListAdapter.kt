package com.example.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ericchee.songdataprovider.Song

class SongListAdapter(songList: List<Song>): RecyclerView.Adapter<SongListAdapter.songViewHolder>(){
    private var listOfSongs: List<Song> = songList.toList()

    var onSongClickListener: ((title: String, artist: String, song: Song) -> Unit)? = null

    var onLongClickListener: ((title: String, artist: String, pos: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return songViewHolder(layoutView)
    }

    override fun getItemCount() = listOfSongs.size

    override fun onBindViewHolder(holder: songViewHolder, position: Int) {
        val eachSong = listOfSongs[position]
        holder.bind(eachSong)
    }

    fun update(updatedList: List<Song>) {
        this.listOfSongs = updatedList
        notifyDataSetChanged()
    }

    fun shuffle(newList: List<Song>) {
        val callback = songDiffCallback(this.listOfSongs, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)

        this.listOfSongs = newList
    }

    inner class songViewHolder(itemView: View): RecyclerView.ViewHolder (itemView) { // 'inner' -- for other class to access
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val ivCover = itemView.findViewById<ImageView>(R.id.ivCover)

        fun bind(eachSong: Song) {
            ivCover.setImageResource(eachSong.smallImageID)
            tvName.text = (eachSong.title + "\n" + eachSong.artist)

            itemView.setOnClickListener {
                onSongClickListener?.invoke(eachSong.title, eachSong.artist, eachSong)
            }

            itemView.setOnLongClickListener {
                onLongClickListener?.invoke(eachSong.title, eachSong.artist, position)
                true;
            }
        }
    }
}
