package com.basecamp.android.data.datasources.ddbb.mappers

import com.basecamp.android.data.datasources.ddbb.models.NewsDTO
import com.basecamp.android.domain.models.News

class NewsMapper () {

    fun map (news: NewsDTO)  =
        News (
            id = null,
            title = news.title,
            text = news.text,
            author = news.author,
            timestamp = news.timestamp,
            mafia = news.mafia,
            picture = news.picture
        )


    fun map (news: NewsDTO, id: String)  =
        News (
            id = id,
            title = news.title,
            text = news.text,
            author = news.author,
            timestamp = news.timestamp,
            mafia = news.mafia,
            picture = news.picture
        )

    fun map (news: News)  =
        NewsDTO (
            title = news.title,
            text = news.text,
            author = news.author,
            timestamp = news.timestamp,
            mafia = news.mafia,
            picture = news.picture
        )
}