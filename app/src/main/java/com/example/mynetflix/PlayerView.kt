package com.example.mynetflix

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.player_view.view.*

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), PlayerListener {

    private lateinit var playerManager: PlayerManager

    init {
        inflate(context, R.layout.player_view, this)

        play_pause_button.setOnClickListener {
            if (playerManager.isPlaying()) {
                playerManager.pause()
            } else {
                playerManager.play()
            }
        }
        initPlayer()
    }

    private fun initPlayer() {
        playerManager = PlayerManager(context = context, listener = this)
        playerManager.setVideoSurface(surface_view)
        playerManager.prepare(Uri.parse("file:/android_asset/example_video.mp4"))
    }

    override fun onError() {
        error_message.text = "Une erreur est survenue veuillez relancer la vidéo"
        error_message.visibility = View.VISIBLE
    }

    override fun onStateChanged(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.IDLE -> play_pause_button.visibility = View.VISIBLE
            is PlayerState.BUFFERING -> {
                play_pause_button.visibility = GONE
                spinner.visibility = View.VISIBLE
            }
            is PlayerState.PLAYING -> {
                play_pause_button.visibility = View.VISIBLE
                play_pause_button.setBackgroundResource(R.drawable.exo_controls_pause)
                spinner.visibility = GONE
            }
            is PlayerState.PAUSED -> {
                play_pause_button.visibility = View.VISIBLE
                play_pause_button.setBackgroundResource(R.drawable.exo_controls_play)
                spinner.visibility = GONE
            }
        }
    }

    override fun onDetachedFromWindow() {
        playerManager.stop()
        super.onDetachedFromWindow()
        Log.d("TAG", "DETACHED")
    }
}