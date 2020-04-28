package com.example.recycleview

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
import com.ericchee.songdataprovider.Song
import kotlinx.android.synthetic.main.fragment_list_songs.*

class SongListFragment : Fragment() {
    private var listOfSongs: Array<Song>? = null;

    private var onSongClickListener: OnSongClickListener? = null

    private var songAdapter: SongListAdapter? = null

//    private var OnSongClickListener: OnSongClickListener? = null

    companion object {
        val TAG : String = SongListFragment::class.java.simpleName
        const val ARG_SONG = "arg_song"

        private const val LIST = "list"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            arguments?.let { args ->
                val songList = args.getParcelableArray(ARG_SONG)
                if (songList != null) {
                    this.listOfSongs = songList as Array<Song>
                }
            }
        } else {
            with(savedInstanceState) {
                listOfSongs = getParcelableArray(LIST) as Array<Song>?
            }
        }
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

        songAdapter = listOfSongs?.toList()?.let { SongListAdapter(it) }
        rvSong.adapter = songAdapter

        songAdapter?.onSongClickListener = { song ->
            onSongClickListener?.onSongClicked(song)
        }



//        var currentPlay: Song? = null

//        songAdapter?.onSongClickListener = { title, artist, song ->
//            tvBanner.text = title.plus(" - ").plus(artist)
//            currentPlay = song;
//        }
//
//        songAdapter.onLongClickListener = { title, artist, pos ->
//            listOfSongs.removeAt(pos)
//            songAdapter.update(listOfSongs)
//            Toast.makeText(this, "$title by $artist was deleted", Toast.LENGTH_SHORT).show()
//        }
//
//        tvBanner.setOnClickListener {
//            if (currentPlay != null) {
//                val intent = Intent(this, PlayerActivity::class.java)
//                intent.putExtra(song_key, currentPlay)
//                startActivity(intent)
//            }
//        }
//

//        btnShuffle.setOnClickListener {
//            val shuffleList = listOfSongs?.map { it.copy() }?.toMutableList()
//            val newList = shuffleList?.apply {
//                shuffle()
//            }
//            if (newList != null) {
//                listOfSongs = newList.toTypedArray()
//                songAdapter?.shuffle(newList)
//            }
//            Log.i("Jason", "WALAWUBIDUDU")
//        }
    }

    fun shuffleList() {
        val shuffleList = listOfSongs?.map { it.copy() }?.toMutableList()

        val newList = shuffleList?.apply {
            shuffle()
        }

        if (newList != null) {
            songAdapter?.shuffle(newList)
            this.listOfSongs = newList.toTypedArray()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArray(LIST, listOfSongs)
        super.onSaveInstanceState(outState)
    }

    class SongListAdapter(songList: List<Song>): RecyclerView.Adapter<SongListAdapter.songViewHolder>(){
        private var listOfSongs: List<Song> = songList

        var onSongClickListener: ((song: Song) -> Unit)? = null

//        var onLongClickListener: ((title: String, artist: String, pos: Int) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songViewHolder {
            val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
            return songViewHolder(layoutView)
        }

        override fun getItemCount() = listOfSongs.size

        override fun onBindViewHolder(holder: songViewHolder, position: Int) {
            val eachSong = listOfSongs[position]
            holder.bind(eachSong)
        }

//        fun update(updatedList: List<Song>) {
//            this.listOfSongs = updatedList
//            notifyDataSetChanged()
//        }

        fun shuffle(newList: List<Song>) {
            val callback = songDiffCallback(this.listOfSongs, newList)
//            Log.i("Jason2", listOfSongs.toString())
//            Log.i("Jason2", newList.toString())
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)

            this.listOfSongs = newList

//            notifyDataSetChanged()
        }

        inner class songViewHolder(itemView: View): RecyclerView.ViewHolder (itemView) { // 'inner' -- for other class to access
            private val tvName = itemView.findViewById<TextView>(R.id.tvName)
            private val ivCover = itemView.findViewById<ImageView>(R.id.ivCover)

            fun bind(eachSong: Song) {
                ivCover.setImageResource(eachSong.smallImageID)
                tvName.text = (eachSong.title + "\n" + eachSong.artist)

                itemView.setOnClickListener {
                    onSongClickListener?.invoke(eachSong)
                }

//                itemView.setOnLongClickListener {
//                    onLongClickListener?.invoke(eachSong.title, eachSong.artist, position)
//                    true;
//                }
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