package com.example.recycleview

import Song
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_list_songs.*
import kotlinx.android.synthetic.main.fragment_main.*


class SongListActivity : AppCompatActivity(), OnSongClickListener {

    companion object {
        private const val CURRENT_SONG = "current_song"
    }

    private var currentPlay: Song? = null

    private var list : List<Song>? = null

    private var songListFragment: SongListFragment? = null

    private lateinit var songManager: SongManager

    private lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        val songList = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as? SongListFragment

        songManager = (application as DotifyApp).songManager

        apiManager = (application as DotifyApp).apiManager

        if (songList == null) {
            apiManager.getSongs ({ songList ->
                songManager.listOfSongs = songList

                songListFragment?.showSongList()

            },
            {
                val dialogBuilder = AlertDialog.Builder(this)

                dialogBuilder.setMessage("No Internet Connection !!!")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Quit",
                        DialogInterface.OnClickListener { dialog, id ->
                            finish()
                        })

                val alert = dialogBuilder.create()
                alert.setTitle("Alert")
                alert.show()
            })
        }


        this.currentPlay = songManager.currentPlay

        if (findPlayingFragment() != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            tvBanner.visibility = View.GONE
            btnShuffle.visibility = View.GONE
        }
        if (currentPlay != null) {
            tvBanner.text = currentPlay?.title.plus(" - ").plus(currentPlay?.artist)
        }

        if (songList == null) {
            songListFragment = SongListFragment.getInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragContainer, songListFragment!!, SongListFragment.TAG)
                .commit()
        }

        tvBanner.setOnClickListener() {

            var nowPlayingFragment = findPlayingFragment()

            if (currentPlay != null) {
                if (nowPlayingFragment == null) {
                    nowPlayingFragment = NowPlayingFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragContainer, nowPlayingFragment, NowPlayingFragment.TAG)
                        .addToBackStack(NowPlayingFragment.TAG)
                        .commit()
                } else {
                    nowPlayingFragment.updateSong(currentPlay!!)
                }
                rvSong.visibility = View.GONE
                tvBanner.visibility = View.GONE
                btnShuffle.visibility = View.GONE
            }

        }

        supportFragmentManager.addOnBackStackChangedListener {
            val hasBackStack = supportFragmentManager.backStackEntryCount > 0
            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        btnShuffle.setOnClickListener {
            if (songList == null) {
                songListFragment?.shuffleList()
            } else {
                songList.shuffleList()
            }
        }

    }

    private fun findPlayingFragment() = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) as? NowPlayingFragment

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()

        rvSong.visibility = View.VISIBLE
        tvBanner.visibility = View.VISIBLE
        btnShuffle.visibility = View.VISIBLE

        return super.onSupportNavigateUp()
    }

    override fun onSongClicked(song: Song) {
        tvBanner.text = song.title.plus(" - ").plus(song.artist)
        currentPlay = song
    }

}

