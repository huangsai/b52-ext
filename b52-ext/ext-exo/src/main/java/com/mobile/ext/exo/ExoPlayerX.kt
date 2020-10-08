package com.mobile.ext.exo

import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.mobile.guava.android.mvvm.AndroidX
import com.mobile.guava.android.text.md5Key
import com.mobile.guava.data.createPoorSSLOkHttpClient
import com.mobile.guava.jvm.Guava
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File

object ExoPlayerX : Player.EventListener, CacheWriter.ProgressListener {

    @Volatile
    private var isInitialized = false

    private var isPlayerCreated = false
    private var mediaSources: List<MediaSource> = emptyList()

    private val eventListeners: ArrayList<Player.EventListener> = ArrayList()
    private val controlDispatcher = DefaultControlDispatcher()

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var okHttpDataSourceFactory: OkHttpDataSourceFactory
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var databaseProvider: ExoDatabaseProvider
    private lateinit var cache: SimpleCache
    private lateinit var cacheDataSourceFactory: CacheDataSource.Factory
    private lateinit var trackSelectorParameters: DefaultTrackSelector.Parameters
    private lateinit var trackSelector: DefaultTrackSelector
    lateinit var player: SimpleExoPlayer
        private set

    val isPlaying: Boolean
        get() = isPlayerCreated && player.isPlaying

    fun initialize() {
        if (isInitialized) {
            return
        }
        isInitialized = true

        databaseProvider = ExoDatabaseProvider(AndroidX.myApp)
        cache = SimpleCache(
            File(AndroidX.myApp.externalCacheDir, "video_manager_disk_cache"),
            LeastRecentlyUsedCacheEvictor(256 * 1024 * 1024),
            databaseProvider
        )
        okHttpClient = createPoorSSLOkHttpClient(TAG_EXO_PLAYER)
        okHttpDataSourceFactory = OkHttpDataSourceFactory(
            okHttpClient,
            ExoPlayerLibraryInfo.DEFAULT_USER_AGENT
        )
        defaultDataSourceFactory = DefaultDataSourceFactory(
            AndroidX.myApp,
            okHttpDataSourceFactory
        )
        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)

        trackSelectorParameters = DefaultTrackSelector
            .ParametersBuilder(AndroidX.myApp)
            .setTunnelingAudioSessionId(C.generateAudioSessionIdV21(AndroidX.myApp))
            .build()
        trackSelector = DefaultTrackSelector(AndroidX.myApp)
        trackSelector.parameters = trackSelectorParameters
    }

    fun createPlayer() {
        if (isPlayerCreated) return

        isPlayerCreated = true
        player = SimpleExoPlayer.Builder(AndroidX.myApp)
            .setUseLazyPreparation(false)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .setTrackSelector(trackSelector)
            .build()

        player.repeatMode = Player.REPEAT_MODE_ALL
        player.setWakeMode(C.WAKE_MODE_NONE)
        player.addListener(this)
    }

    fun destroyPlayer() {
        if (!isPlayerCreated) return

        isPlayerCreated = false
        player.removeListener(this)
        player.release()
    }

    fun snapshotSource(): ExoSource {
        return ExoSource(
            player.currentPosition,
            player.currentWindowIndex,
            mediaSources
        )
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        eventListeners.forEach {
            it.onTimelineChanged(timeline, reason)
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        eventListeners.forEach {
            it.onMediaItemTransition(mediaItem, reason)
        }
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
        eventListeners.forEach {
            it.onTracksChanged(trackGroups, trackSelections)
        }
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        eventListeners.forEach {
            it.onIsLoadingChanged(isLoading)
        }
    }

    override fun onPlaybackStateChanged(state: Int) {
        eventListeners.forEach {
            it.onPlaybackStateChanged(state)
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        eventListeners.forEach {
            it.onPlayWhenReadyChanged(playWhenReady, reason)
        }
    }

    override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
        eventListeners.forEach {
            it.onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        eventListeners.forEach {
            it.onIsPlayingChanged(isPlaying)
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        eventListeners.forEach {
            it.onRepeatModeChanged(repeatMode)
        }
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        eventListeners.forEach {
            it.onShuffleModeEnabledChanged(shuffleModeEnabled)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Timber.tag(TAG_EXO_PLAYER).d(error)
        eventListeners.forEach {
            it.onPlayerError(error)
        }
    }

    override fun onPositionDiscontinuity(reason: Int) {
        eventListeners.forEach {
            it.onPositionDiscontinuity(reason)
        }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        eventListeners.forEach {
            it.onPlaybackParametersChanged(playbackParameters)
        }
    }

    override fun onExperimentalOffloadSchedulingEnabledChanged(offloadSchedulingEnabled: Boolean) {
        eventListeners.forEach {
            it.onExperimentalOffloadSchedulingEnabledChanged(offloadSchedulingEnabled)
        }
    }

    override fun onProgress(requestLength: Long, bytesCached: Long, newBytesCached: Long) {
        Timber.tag(TAG_EXO_PLAYER).d(
            "requestLength %s -bytesCached %s -newBytesCached %s",
            requestLength,
            bytesCached,
            newBytesCached
        )
    }

    private fun createMediaSource(
        @C.ContentType contentType: Int,
        mediaItem: MediaItem
    ): MediaSource {
        if (contentType == C.TYPE_DASH) {
            return DashMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
        }

        if (contentType == C.TYPE_SS) {
            return SsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
        }

        if (contentType == C.TYPE_HLS) {
            return HlsMediaSource.Factory(cacheDataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(mediaItem)
        }

        return ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
    }

    private fun createCacheWriter(dataSpec: DataSpec): CacheWriter {
        return CacheWriter(
            cacheDataSourceFactory.createDataSource(),
            dataSpec,
            true,
            null,
            this
        )
    }

    fun addEventListener(l: Player.EventListener) {
        if (!eventListeners.contains(l)) {
            eventListeners.add(l)
        }
    }

    fun removeEventListener(l: Player.EventListener) {
        if (eventListeners.contains(l)) {
            eventListeners.remove(l)
        }
    }

    fun resume() {
        controlDispatcher.dispatchSetPlayWhenReady(player, true)
    }

    fun pause() {
        controlDispatcher.dispatchSetPlayWhenReady(player, false)
    }

    fun stop() {
        controlDispatcher.dispatchStop(player, true)
    }

    fun play(source: ExoSource) {
        mediaSources = source.mediaSources

        player.seekTo(source.windowIndex, source.position)
        player.setMediaSources(source.mediaSources, true)
        player.prepare()
    }

    fun play(vararg uris: Uri) {
        mediaSources = uris.map {
            createMediaSource(
                Util.inferContentType(it),
                MediaItem.Builder().setTag(it.md5Key()).setUri(it).build()
            )
        }
        player.setMediaSources(mediaSources)
        player.prepare()
    }

    fun preload(uri: Uri) = GlobalScope.launch(Dispatchers.IO) {
        try {
            val requestCacheLength = if (Guava.isDebug) {
                32L * 1024L
            } else {
                128L * 1024L
            }

            val cachingKey = uri.md5Key()
            val dataSpec = DataSpec.Builder()
                .setUri(uri)
                .setPosition(0L)
                .setLength(requestCacheLength)
                .setKey(cachingKey)
                .build()

            val cached = cache.getCachedBytes(cachingKey, 0, requestCacheLength)
            if (cached > 0) {
                Timber.tag(TAG_EXO_PLAYER).d("skip preload")
            } else {
                createCacheWriter(dataSpec).cache()
            }
        } catch (e: Exception) {
            Timber.tag(TAG_EXO_PLAYER).d(e.message ?: "preload error")
        }
    }
}