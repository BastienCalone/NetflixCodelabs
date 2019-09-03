package com.example.mynetflix

interface PlayerListener {
    fun onError()
    fun onStateChanged(playerState: PlayerState)
}