package com.basecamp.android.core.main.info.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.popkorn.inject
import com.basecamp.android.core.main.info.views.InfoListItemView
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.Info

class InfoRecyclerViewAdapter: RecyclerView.Adapter<InfoRecyclerViewAdapter.MyViewHolder>() {

    private val news: MutableList<Info> = mutableListOf()
    private var onEditClickListener: ((String) -> Unit) = {}
    private val settingsPreferences = inject<SettingsPreferences>()
    class MyViewHolder(val view: InfoListItemView) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = InfoListItemView(parent.context)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        v.layoutParams = lp
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val new = news[position]
        holder.view.apply {
            setInfo(new, settingsPreferences.getCanWrite(), onEditClickListener)
        }
    }

    override fun getItemCount() = news.size

    fun setData (news: List<Info>) {
        this.news.clear()
        this.news.addAll(news)
        notifyDataSetChanged()
    }

    fun setOnEditClickListener (onClickListener: (String) -> Unit){
        this.onEditClickListener = onClickListener
    }

}