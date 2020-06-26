package com.basecamp.android.core.main.info.add

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.Info
import com.basecamp.android.domain.usecases.GetInfoUseCase
import com.basecamp.android.domain.usecases.UpdateInfoUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class AddInfoPresenter(
    private val settingsPreferences: SettingsPreferences,
    private val updateInfoUseCase: UpdateInfoUseCase,
    private val getInfosUseCase: GetInfoUseCase
) : Presenter<AddInfoContract.View, AddInfoContract.Router>(), AddInfoContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)
    private val isDarkMode by lazy { settingsPreferences.getDarkMode() }
    var info: Info = Info()

    override fun getPageName(): String = "AddNew"

    override fun init(bundle: Bundle) {}

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
    }

    override fun getInfo(id: String) {
        coroutineScope.launch {
            var infoId: Info? = null
            withContext(Dispatchers.IO) {
                val response = getInfosUseCase.getInfo(id)
                infoId = if (response is ResponseState.Success) {
                    response.result
                } else {
                    null
                }
            }
            infoId?.let {
                info = it
                draw { setInformation(it) }
            }
        }
    }

    override fun onSaveClick(title: String, text: String) {
        info.apply {
            this.title = title
            this.text = text
            this.mafia = isDarkMode
        }
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = if (info.id == null) updateInfoUseCase.createInfo(info) else updateInfoUseCase.updateInfo(info)
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
        info.id?.let{id ->
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    val response = updateInfoUseCase.deleteInfo(id)
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