package com.basecamp.android.core.common.views

import android.content.Context
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

private val max_cache_size : Long = 25 * 1024 * 1024  // 25 MB
private val max_cache_file_size : Long = 10 * 1024 * 1024 // 10 MB
private var simpleCache: SimpleCache?=null

fun Context.getSimpleCache() : SimpleCache {
    return simpleCache ?: SimpleCache(
                File(cacheDir, "media"),
                LeastRecentlyUsedCacheEvictor(max_cache_size)).also { simpleCache = it }
}


class LocalCacheDataSourceFactory(private val context: Context) : DataSource.Factory {
    private val defaultDataSourceFactory: DefaultDataSourceFactory

    private val cacheDataSink: CacheDataSink = CacheDataSink(context.getSimpleCache(), max_cache_file_size )
    private val fileDataSource: FileDataSource = FileDataSource()

    init {
        val userAgent = "Oysho"
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDataSourceFactory = DefaultDataSourceFactory(
                this.context,
                bandwidthMeter,
                DefaultHttpDataSourceFactory(userAgent)
        )
    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
                simpleCache, defaultDataSourceFactory.createDataSource(),
                fileDataSource, cacheDataSink,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}