package com.basecamp.android.core.main.feed.add

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.News
import com.basecamp.android.domain.usecases.GetNewsUseCase
import com.basecamp.android.domain.usecases.UpdateNewsUseCase
import kotlinx.coroutines.*
import java.util.*

@Injectable(Scope.BY_NEW)
class AddNewPresenter(
    private val settingsPreferences: SettingsPreferences,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : Presenter<AddNewContract.View, AddNewContract.Router>(), AddNewContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)
    private val isDarkMode by lazy { settingsPreferences.getDarkMode() }
    var news: News = News()

    override fun getPageName(): String = "AddNew"

    override fun init(bundle: Bundle) {
        draw { darkVersion(isDarkMode) }

    }

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
    }

    override fun getNews(id: String) {
        coroutineScope.launch {
            var newsId: News? = null
            withContext(Dispatchers.IO) {
                val response = getNewsUseCase.getNews(id)
                newsId = if (response is ResponseState.Success) {
                    response.result
                } else {
                    null
                }
            }
            newsId?.let {
                news = it
                draw { setInformation(it) }
            }
        }
    }

    override fun onSaveClick(picture: String?, title: String, text: String, author: Int?) {
        news.apply {
            this.title = title
            this.text = text
            this.author = author
            if (timestamp == null) timestamp = Date().time
            this.mafia = isDarkMode
            this.picture = picture
        }
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = if (news.id == null) updateNewsUseCase.createNews(news) else updateNewsUseCase.updateNews(news)
                if (response is ResponseState.Success) {
                    navigate { closeDialog() }
                }
                else {
                    draw { setError((response as ResponseState.Failure).ex.localizedMessage ?: "Something went wrong, try again later") }
                }
            }
        }
    }

    override fun onDeleteClick(){
        news.id?.let{id ->
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    val response = updateNewsUseCase.deleteNews(id)
                    if (response is ResponseState.Success) {
                        navigate { closeDialog() }
                    }
                    else {
                        draw { setError((response as ResponseState.Failure).ex.localizedMessage ?: "Something went wrong, try again later") }
                    }
                }
            }
        }
    }

}