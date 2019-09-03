package com.example.mynetflix

import android.content.Context
import android.net.Uri
import android.view.SurfaceView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class PlayerManager(val context: Context, val listener: PlayerListener) : Player.EventListener {

    private var player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)

    init {
        player.addListener(this)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val factorymediaSourceFactory: ExtractorMediaSource.Factory =
            ExtractorMediaSource.Factory(
                DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, "MyNetflix")
                )
            )
        return factorymediaSourceFactory.createMediaSource(uri)
    }

    fun setVideoSurface(surfaceview: SurfaceView) {
        player.setVideoSurfaceView(surfaceview)
    }

    fun prepare(uri: Uri) {
        player.prepare(buildMediaSource(uri), false, true);
    }

    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun stop() {
        player.stop(true)
        player.seekToDefaultPosition()
    }

    fun isPlaying(): Boolean {
        return player.playWhenReady
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> listener.onStateChanged(PlayerState.IDLE)
            Player.STATE_BUFFERING -> listener.onStateChanged(PlayerState.BUFFERING)
            Player.STATE_READY -> if (playWhenReady) {
                listener.onStateChanged(PlayerState.PLAYING)
            } else {
                listener.onStateChanged(PlayerState.PAUSED)
            }
            Player.STATE_ENDED -> listener.onStateChanged(PlayerState.ENDED)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        listener.onError()
    }
}