package com.basecamp.android.core.main.info.families

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.models.Family
import com.basecamp.android.domain.usecases.GetFamiliesUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class FamiliesPresenter(
    private val getFamiliesUseCase: GetFamiliesUseCase
) : Presenter<FamiliesContract.View, FamiliesContract.Router>(), FamiliesContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Families"

    override fun init(bundle: Bundle) {
        geFamilies()
    }

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
    }

    private fun geFamilies() {
        coroutineScope.launch {
            var families: List<Family>? = emptyList()
            withContext(Dispatchers.IO) {
                val response = getFamiliesUseCase.getFamilies()
                if (response is ResponseState.Success) {
                    families = response.result
                } else {
                    families = null
                }
            }
            families?.let { info_list ->
                info_list.takeIf { info_list.isNotEmpty() }?.let {
                    draw { setInformation(it) }
                } ?: draw { setEmpty() }
            } ?: draw { setError() }
        }
    }
}