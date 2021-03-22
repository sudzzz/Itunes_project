package com.example.itunes.model

data class Track(
        val trackId: Int,
        val trackName: String,
        val trackTime: Int,
        val primaryGenreName : String,
        val artistName: String,
        val artistId: Int
)