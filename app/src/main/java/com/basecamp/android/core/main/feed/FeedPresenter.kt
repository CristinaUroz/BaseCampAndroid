package com.basecamp.android.core.main.feed

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.News
import com.basecamp.android.domain.usecases.GetNewsUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class FeedPresenter(
    private val settingsPreferences: SettingsPreferences,
    private val getNewsUseCase: GetNewsUseCase
) : Presenter<FeedContract.View, FeedContract.Router>(), FeedContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)
    private val isDarkMode by lazy { settingsPreferences.getDarkMode() }

    override fun getPageName(): String = "Feed"

    override fun init(bundle: Bundle) {
        if (settingsPreferences.getCanWrite()) {
            draw { showCanWrite() }
        }
        getNews()
        draw { setOnRefreshingCallback { getNews() } }
    }

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(true) }
    }

    private fun getNews() {
        coroutineScope.launch {
            var news: List<News>? = emptyList()
            if (isDarkMode) {
                withContext(Dispatchers.IO) {
                    val response = getNewsUseCase.getMafiaNews()
                    if (response is ResponseState.Success) {
                        news = response.result
                    } else {
                        news = null
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    val response = getNewsUseCase.getNormalNews()
                    news = if (response is ResponseState.Success) {
                        response.result
                    } else {
                        null
                    }
                }
            }
            news?.let { news_list ->
                news_list.takeIf { news_list.isNotEmpty() }?.let {
                    draw { setInformation(it) }
                } ?: draw { setEmpty() }
            } ?: draw { setError() }
            draw { setRefreshing(false) }
        }
    }


}