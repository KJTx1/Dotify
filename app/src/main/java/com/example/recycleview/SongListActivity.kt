package com.example.recycleview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.fragment_list_songs.*
import kotlinx.android.synthetic.main.fragment_main.*


class SongListActivity : AppCompatActivity(), OnSongClickListener {

    companion object {
        private const val CURRENT_SONG = "current_song"
    }

    private var currentPlay: Song? = null

    private var list : Array<Song>? = null

    private var songListFragment: SongListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        if (findPlayingFragment() != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            tvBanner.visibility = View.INVISIBLE
            btnShuffle.visibility = View.INVISIBLE
        }

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                currentPlay = getParcelable(CURRENT_SONG)
                if (currentPlay != null) {
                    tvBanner.text = currentPlay?.title.plus(" - ").plus(currentPlay?.artist)
                }
            }
        }

        val songList = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as? SongListFragment

        if (songList == null) {
            songListFragment = SongListFragment()
            val argumentBundle = Bundle().apply {
                list = SongDataProvider.getAllSongs().toTypedArray()
                putParcelableArray(SongListFragment.ARG_SONG, list)
            }
            songListFragment!!.arguments = argumentBundle
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
                    val playingBundle = Bundle().apply {
                        putParcelable(NowPlayingFragment.ARG_SONG, currentPlay)
                    }
                    nowPlayingFragment.arguments = playingBundle
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(CURRENT_SONG, currentPlay)
        super.onSaveInstanceState(outState)
    }

}

