package com.example.recycleview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ericchee.songdataprovider.Song
import kotlinx.android.synthetic.main.fragment_now_playing.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class NowPlayingFragment : Fragment() {

    companion object {
        val TAG : String = NowPlayingFragment::class.java.simpleName
//        private const val SAVE_SONG = "save_song"
        private const val COUNT = "count"
        const val ARG_SONG = "arg_song"
    }

    private var song: Song? = null
    private var randomNumber : Int? = null
    private var playNum : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                randomNumber = getInt(COUNT, -1)
            }
        } else {
            randomNumber = Random.nextInt(0, 100000000)
        }

        arguments?.let { args ->
            song = args.getParcelable<Song>(ARG_SONG)
        }
        playNum = randomNumber
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
            playNum = playNum?.plus(1)
            tvNumOfPlay.text = "$playNum plays"
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
            imageView.setImageResource(it.largeImageID)
            tvSongTitle.text = it.title
            tvLyrics.text = it.artist
            tvNumOfPlay.text = randomNumber.toString().plus(" plays")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        playNum?.let { outState.putInt(COUNT, it) }
        super.onSaveInstanceState(outState)
    }
}
