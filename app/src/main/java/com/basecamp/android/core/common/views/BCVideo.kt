package com.basecamp.android.core.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.basecamp.android.R
import com.basecamp.android.core.common.helpers.AttrHelper
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.common.api.Releasable

class BCVideo (context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), Releasable {

    private var looping = true

    private var mediaPlayer : ExoPlayerController ?= null
    private val player by lazy {findViewById<PlayerView>(R.id.bc_video_player_view)}

    private var videoUrl : String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.bc_video, this)
        val conf = AttrHelper.from(getContext(), attrs, R.attr.bc_loop)
        looping = conf.getBoolean(R.attr.bc_loop, true)
    }

    fun setVideo(url: String) {
        videoUrl = url
        mediaPlayer = mediaPlayer ?: createMediaPlayer()
        mediaPlayer?.play(url)
    }

    private fun createMediaPlayer() : ExoPlayerController {
//        player.useController = false
        val exoController = ExoPlayerController()
        player.player = exoController.getPlayerImpl(context)
        exoController.setLooping(looping)
        return exoController
    }

    override fun onAttachedToWindow() {
        mediaPlayer?.resume() ?: videoUrl?.also { setVideo(it) }
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        mediaPlayer?.pause()
        super.onDetachedFromWindow()
    }

    override fun release() {
        mediaPlayer?.releasePlayer()
        mediaPlayer = null
    }
}
