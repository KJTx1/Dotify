package com.example.recycleview

import Song

class SongManager {
    var listOfSongs: List<Song>
    var currentPlay: Song? = null

    init {
        listOfSongs = emptyList()
    }



    fun shuffle() {
        listOfSongs = listOfSongs.toMutableList().apply {
            shuffle()
        }.toList()
    }
}