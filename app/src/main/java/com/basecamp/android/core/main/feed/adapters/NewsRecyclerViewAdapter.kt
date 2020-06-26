package com.basecamp.android.core.main.feed.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.popkorn.inject
import com.basecamp.android.core.main.feed.views.NewsListItemView
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.News

class NewsRecyclerViewAdapter: RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder>() {

    private val news: MutableList<News> = mutableListOf()
    private var onClickListener: ((News) -> Unit)? = null
    private var onEditClickListener: ((String) -> Unit) = {}
    private val settingsPreferences = inject<SettingsPreferences>()
    class MyViewHolder(val view: NewsListItemView) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = NewsListItemView(parent.context)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        v.layoutParams = lp
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val new = news[position]
        holder.view.apply {
            setInfo(new, settingsPreferences.getCanWrite(), onEditClickListener)
            setOnClickListener { onClickListener?.invoke(new) }
        }
    }

    override fun getItemCount() = news.size

    fun setData (news: List<News>) {
        this.news.clear()
        this.news.addAll(news)
        notifyDataSetChanged()
    }

    fun setOnClickListener (onClickListener: (News) -> Unit){
        this.onClickListener = onClickListener
    }


    fun setOnEditClickListener (onClickListener: (String) -> Unit){
        this.onEditClickListener = onClickListener
    }

    fun getItemByPosition(pos: Int): News{
        return this.news[pos]
    }
}