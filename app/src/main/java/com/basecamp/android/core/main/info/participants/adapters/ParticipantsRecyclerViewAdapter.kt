package com.basecamp.android.core.main.info.participants.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basecamp.android.core.main.info.participants.views.UserListItemView
import com.basecamp.android.domain.models.User

class ParticipantsRecyclerViewAdapter: RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.MyViewHolder>() {

    private val users: MutableList<User> = mutableListOf()
    private var onUserClick: (View, User) -> Unit = {_,_ -> }
    class MyViewHolder(val view: UserListItemView) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = UserListItemView(parent.context)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        v.layoutParams = lp
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        holder.view.apply {
            setUser(user)
            setOnClickListener{onUserClick.invoke(profilePicture, user)}
        }
    }

    override fun getItemCount() = users.size

    fun setOnUserClick(onUserClick: (View, User) -> Unit){
        this.onUserClick = onUserClick
    }

    fun setData (news: List<User>) {
        this.users.clear()
        this.users.addAll(news)
        notifyDataSetChanged()
    }

}