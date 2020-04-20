package com.example.recycleview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.example.recycleview.PlayerActivity.Companion.song_key
import kotlinx.android.synthetic.main.activity_main.*


class SongListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listOfSongs = SongDataProvider.getAllSongs().toMutableList()
        val songAdapter = SongListAdapter(listOfSongs)
        var currentPlay: Song? = null

        songAdapter.onSongClickListener = { title, artist, song ->
            tvBanner.text = title.plus(" - ").plus(artist)
            currentPlay = song;
        }

        songAdapter.onLongClickListener = { title, artist, pos ->
            listOfSongs.removeAt(pos)
            songAdapter.update(listOfSongs)
            Toast.makeText(this, "$title by $artist was deleted", Toast.LENGTH_SHORT).show()
        }

        tvBanner.setOnClickListener {
            if (currentPlay != null) {
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(song_key, currentPlay)
                startActivity(intent)
            }
        }

        btnShuffle.setOnClickListener {
            val shuffleList = listOfSongs.map { it.copy() }.toMutableList()
            val newList = shuffleList.apply {
                shuffle()
            }
            listOfSongs = newList
            songAdapter.shuffle(newList)
        }

        rvSong.adapter = songAdapter

    }

}

