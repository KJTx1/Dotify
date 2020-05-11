package com.example.recycleview

import Song
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list_songs.*

class SongListFragment : Fragment() {
    private var listOfSongs: List<Song>? = null;

    private var onSongClickListener: OnSongClickListener? = null

    private var songAdapter: SongListAdapter? = null

    private lateinit var songManager: SongManager

    companion object {
        val TAG: String = SongListFragment::class.java.simpleName

        fun getInstance(): SongListFragment {
            return SongListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        songManager = (context.applicationContext as DotifyApp).songManager

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.listOfSongs = songManager.listOfSongs

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_list_songs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = listOfSongs?.let { SongListAdapter(it) }
        rvSong.adapter = songAdapter

        songAdapter?.onSongClickListener = { song ->

            songManager.currentPlay = song

            onSongClickListener?.onSongClicked(songManager.currentPlay!!)

        }

    }

    fun shuffleList() {
        songManager.shuffle()
        listOfSongs = songManager.listOfSongs
        songAdapter?.shuffle(listOfSongs!!)
    }

    fun showSongList() {
        songAdapter?.load(songManager.listOfSongs)
    }


    class SongListAdapter(songList: List<Song>): RecyclerView.Adapter<SongListAdapter.songViewHolder>(){
        private var listOfSongs: List<Song> = songList

        var onSongClickListener: ((song: Song) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songViewHolder {
            val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
            return songViewHolder(layoutView)
        }

        override fun getItemCount() = listOfSongs.size

        override fun onBindViewHolder(holder: songViewHolder, position: Int) {
            val eachSong = listOfSongs[position]
            holder.bind(eachSong)
        }

        fun shuffle(newList: List<Song>) {
            val callback = songDiffCallback(this.listOfSongs, newList)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)

            this.listOfSongs = newList
        }

        fun load(theList: List<Song>) {
            this.listOfSongs = theList
            notifyDataSetChanged()
        }

        inner class songViewHolder(itemView: View): RecyclerView.ViewHolder (itemView) { // 'inner' -- for other class to access
            private val tvName = itemView.findViewById<TextView>(R.id.tvName)
            private val ivCover = itemView.findViewById<ImageView>(R.id.ivCover)

            fun bind(eachSong: Song) {
//                ivCover.setImageResource(eachSong.smallImageURL)
                Picasso.get().load(eachSong.smallImageURL).into(ivCover)
                tvName.text = (eachSong.title + "\n" + eachSong.artist)

                itemView.setOnClickListener {
                    onSongClickListener?.invoke(eachSong)
                }
            }
        }
    }

    class songDiffCallback(
        private val oldList: List<Song>,
        private val newList: List<Song>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSong = oldList[oldItemPosition]
            val newSong = newList[newItemPosition]
            return oldSong.id == newSong.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSong = oldList[oldItemPosition]
            val newSong = newList[newItemPosition]
            return oldSong.title == newSong.title
        }
    }

}

interface OnSongClickListener {
    fun onSongClicked(song: Song)
}