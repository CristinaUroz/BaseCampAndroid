package com.basecamp.android.core.main.info.families.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.basecamp.android.core.main.info.families.views.FamilyView
import com.basecamp.android.domain.models.Family

class FamiliesPagerAdapter: PagerAdapter() {

    private val families: MutableList<Family> = mutableListOf()

    override fun getItemPosition(obj: Any): Int = families.indexOf(obj).let { if (it<0) POSITION_NONE else it}

    override fun getCount(): Int = families.size

    override fun instantiateItem(container: ViewGroup, pos: Int): Any {
        val mediaFragment = FamilyView(container.context)

        mediaFragment.setFamily(families[pos])
        container.addView(mediaFragment)
        return mediaFragment
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean  = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }


    fun setData (list: List<Family>){
        this.families.clear()
        this.families.addAll(list)
        notifyDataSetChanged()
    }

}