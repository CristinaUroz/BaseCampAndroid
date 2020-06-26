package com.basecamp.android.data.datasources.ddbb.mappers

import com.basecamp.android.data.datasources.ddbb.models.InfoDTO
import com.basecamp.android.domain.models.Info

class InfoMapper () {

    fun map (info: InfoDTO)  =
        Info (
            id = null,
            title = info.title,
            text = info.text,
            mafia = info.mafia
        )


    fun map (info: InfoDTO, id: String)  =
        Info (
            id = id,
            title = info.title,
            text = info.text,
            mafia = info.mafia
        )

    fun map (info: Info)  =
        InfoDTO (
            title = info.title,
            text = info.text,
            mafia = info.mafia
        )
}