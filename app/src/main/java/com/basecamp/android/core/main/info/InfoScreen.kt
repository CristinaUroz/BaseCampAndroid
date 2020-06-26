package com.basecamp.android.core.main.info

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.main.info.adapters.InfoRecyclerViewAdapter
import com.basecamp.android.core.main.info.common.MultiTapDetector
import com.basecamp.android.domain.models.Info
import kotlin.reflect.KClass


class InfoScreen : Screen<InfoPresenter>(), InfoContract.View, InfoContract.Router {

    private val newsRecyclerViewAdapter = InfoRecyclerViewAdapter()
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.screen_info_list_recycler_view) }
    private val frameContainer by lazy { findViewById<RelativeLayout>(R.id.screen_info_list_frame_container) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_info_list_progress_bar) }
    private val addButton by lazy { findViewById<ImageView>(R.id.screen_info_add_button) }
    private val versionNumber by lazy { findViewById<TextView>(R.id.screen_info_version_number) }
    private val familiesButton by lazy { findViewById<ConstraintLayout>(R.id.screen_info_families) }
    private val participantsButton by lazy { findViewById<ConstraintLayout>(R.id.screen_info_participants) }

    override fun getLayout(): Int = R.layout.screen_info

    override fun getPresenter(): KClass<InfoPresenter> = InfoPresenter::class

    override fun init() {
        val versionText = "${context?.getString(R.string.version)}: ${context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName}"
        versionNumber.text = versionText
        MultiTapDetector(versionNumber) { i, _ ->
            if (i >= 5) {
                notify { onEnterDarkModeClick() }
            }
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsRecyclerViewAdapter
        }
        newsRecyclerViewAdapter.apply {
            setOnEditClickListener {
                navigate(InfoScreenDirections.actionInfoScreenToAddinfoScreen(it))
            }
        }
        addButton.setOnClickListener {
            navigate(InfoScreenDirections.actionInfoScreenToAddinfoScreen())
        }

        participantsButton.setOnClickListener {
            navigate(InfoScreenDirections.actionInfoScreenToParticipantsScreen())
        }

        familiesButton.setOnClickListener {
            navigate(InfoScreenDirections.actionInfoScreenToFamiliesScreen())
        }
    }

    override fun showCanWrite() {
        addButton.visibility = View.VISIBLE
    }

    override fun setInformation(list: List<Info>) {
        newsRecyclerViewAdapter.setData(list)
        recyclerView.visibility = View.VISIBLE
        setProgressDialog(false)
        if (list.isNotEmpty()) {
            frameContainer.removeAllViews()
            frameContainer.visibility = View.GONE
        }
    }

    override fun setDarkMode(b: Boolean) {
        if (b) {
            versionNumber.visibility = View.GONE
            familiesButton.visibility = View.VISIBLE
        } else {
            versionNumber.visibility = View.VISIBLE
            familiesButton.visibility = View.GONE
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

}