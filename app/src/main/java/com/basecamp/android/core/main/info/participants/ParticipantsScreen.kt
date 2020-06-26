package com.basecamp.android.core.main.info.participants

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import cc.popkorn.inject
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.main.info.participants.adapters.ParticipantsRecyclerViewAdapter
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User
import kotlin.reflect.KClass

class ParticipantsScreen : Screen<ParticipantsPresenter>(), ParticipantsContract.View, ParticipantsContract.Router {

    private val newsRecyclerViewAdapter = ParticipantsRecyclerViewAdapter()
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.screen_participants_list_recycler_view) }
    private val frameContainer by lazy { findViewById<RelativeLayout>(R.id.screen_participants_list_frame_container) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_participants_list_progress_bar) }

    private var isLoadingMoreItems = false

    private val settingsPreferences = inject<SettingsPreferences>()
    override fun getLayout(): Int = R.layout.screen_participants

    override fun getPresenter(): KClass<ParticipantsPresenter> = ParticipantsPresenter::class

    override fun init() {
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = newsRecyclerViewAdapter
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        newsRecyclerViewAdapter.setOnUserClick { view, user ->
            navigate(ParticipantsScreenDirections.actionParticipantsScreenToUserScreen(user), FragmentNavigatorExtras(view to user.email))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setExitToFullScreenTransition()
        setReturnFromFullScreenTransition()
    }

    override fun setInformation(list: List<User>) {
        newsRecyclerViewAdapter.setData(list)
        recyclerView.visibility = View.VISIBLE
        setProgressDialog(false)
        if (list.isNotEmpty()) {
            frameContainer.removeAllViews()
            frameContainer.visibility = View.GONE
        }
    }

    override fun setError(lambda: () -> Unit) {
        progressBar.visibility = View.GONE
        frameContainer.removeAllViews()
        frameContainer.visibility = View.VISIBLE
        //TODO frameContainer.addView(ErrorImageView(context, lambda).apply { id = R.id.routes_list_view_error_screen })
    }

    private fun setProgressDialog(b: Boolean) {
        progressBar.visibility = if (b) {
            frameContainer.removeAllViews()
            frameContainer.visibility = View.GONE
            View.VISIBLE
        } else View.GONE
    }

    override fun setEmpty(emptyText: String?, emptyButton: String?, emptyButtonCallback: () -> Unit) {
        progressBar.visibility = View.GONE
        frameContainer.removeAllViews()
        frameContainer.visibility = View.VISIBLE
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        //TODO frameContainer.addView(EmptyScreenImageView(context,emptyText, emptyButton, emptyButtonCallback).apply { id = R.id.routes_list_view_empty_screen }, layoutParams)
    }

    private fun setExitToFullScreenTransition() {
        exitTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.user_list_exit_transition)
    }

    private fun setReturnFromFullScreenTransition() {
        reenterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.user_list_return_transition)
    }
}