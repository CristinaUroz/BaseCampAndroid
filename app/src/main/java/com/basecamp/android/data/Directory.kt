package com.basecamp.android.data


import android.content.Context
import android.os.Environment
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import java.io.File


@Injectable(Scope.BY_APP)
class Directory(context:Context){

    private val publicDir = File(Environment.getExternalStorageDirectory(), "BaseCamp")
    private val cacheDir = context.externalCacheDir ?: context.cacheDir

    val mediaDir:File
        get() { return File(publicDir, "BaseCamp Media").apply { mkdirs() } }

    val tmpDir : File
        get() { return cacheDir.apply { mkdirs() } }

}