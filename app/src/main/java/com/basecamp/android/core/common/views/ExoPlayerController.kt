package com.basecamp.android.core.common.views

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

class ExoPlayerController {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context

    private val extractorMediaSource by lazy {
        ExtractorMediaSource.Factory(LocalCacheDataSourceFactory(context))
    }

    fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        return exoPlayer
    }

    fun setLooping(looping: Boolean){
        exoPlayer.repeatMode = if (looping) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
    }

    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl)
    }

    fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun resume() {
        exoPlayer.playWhenReady = true
    }


    fun play(url: String) {
        val mediaSource = extractorMediaSource.createMediaSource(Uri.parse(url))
        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true
    }

}