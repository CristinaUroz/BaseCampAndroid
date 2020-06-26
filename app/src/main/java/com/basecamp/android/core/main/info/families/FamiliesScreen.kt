package com.basecamp.android.core.main.info.families

import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.main.info.families.adapters.FamiliesPagerAdapter
import com.basecamp.android.core.main.info.families.views.ScrollPoints
import com.basecamp.android.domain.models.Family
import kotlin.reflect.KClass

class FamiliesScreen : Screen<FamiliesPresenter>(), FamiliesContract.View, FamiliesContract.Router {

    private lateinit var familiesAdapter: FamiliesPagerAdapter

    private val viewPager by lazy { findViewById<ViewPager>(R.id.screen_families_pager) }
    private val frameContainer by lazy { findViewById<RelativeLayout>(R.id.screen_families_frame_container) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_families_progress_bar) }
    private val scrollPoints by lazy { findViewById<ScrollPoints>(R.id.screen_families_scroll_points) }

    override fun getLayout(): Int = R.layout.screen_families

    override fun getPresenter(): KClass<FamiliesPresenter> = FamiliesPresenter::class

    override fun init() {

        familiesAdapter = FamiliesPagerAdapter()
        viewPager.adapter = familiesAdapter

    }

    private fun setPointScrollPoint(num: Int){
        scrollPoints.setPoints(num)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) { scrollPoints.setPosition(position) }
        })
    }

    override fun setInformation(list: List<Family>) {
        familiesAdapter.setData(list)
        setPointScrollPoint(list.size)
        viewPager.visibility = View.VISIBLE
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
}