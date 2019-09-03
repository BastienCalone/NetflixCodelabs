package com.example.mynetflix

sealed class PlayerState {
    object IDLE : PlayerState()
    object BUFFERING : PlayerState()
    object PLAYING : PlayerState()
    object PAUSED : PlayerState()
    object ENDED : PlayerState()
}
