package com.example.recycleview

import Song
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_now_playing.*

class NowPlayingFragment : Fragment() {

    private lateinit var dotifyApp: DotifyApp

    companion object {
        val TAG : String = NowPlayingFragment::class.java.simpleName
    }

    private var song: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dotifyApp = context?.applicationContext as DotifyApp

        song = dotifyApp.songManager.currentPlay
    }

    fun updateSong(song: Song) {
        this.song = song
        updateSongView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserName.isEnabled = false

        updateSongView()
        ivPlay.setOnClickListener {
            dotifyApp.listenCount = dotifyApp.listenCount.plus(1)
            if (dotifyApp.listenCount == 1) {
                tvNumOfPlay.text = "${dotifyApp.listenCount} play in total"
            } else {
                tvNumOfPlay.text = "${dotifyApp.listenCount} plays in total"
            }
        }

        btnChangeUser.setOnClickListener {
            if (btnChangeUser.text == "CHANGE USER") {
                btnChangeUser.text = "APPLY"
                tvUserName.isEnabled = true
            } else if (tvUserName.text.toString() != "") {
                btnChangeUser.text = "CHANGE USER"
                tvUserName.isEnabled = false
            }
        }

        ivSkipPre.setOnClickListener {
            Toast.makeText(activity, "Skipping to previous track", Toast.LENGTH_SHORT).show()
        }

        ivSkipNext.setOnClickListener {
            Toast.makeText(activity, "Skipping to next track", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSongView() {
        song?.let {
            Picasso.get().load(it.largeImageURL).into(imageView)
            tvSongTitle.text = it.title
            tvLyrics.text = it.artist
            if (dotifyApp.listenCount == 0 || dotifyApp.listenCount == 1) {
                tvNumOfPlay.text = dotifyApp.listenCount.toString().plus(" play in total")
            } else {
                tvNumOfPlay.text = dotifyApp.listenCount.toString().plus(" plays in total")
            }
        }
    }
}
