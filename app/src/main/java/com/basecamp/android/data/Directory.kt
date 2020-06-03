package com.basecamp.android.data


import android.content.Context
import android.os.Environment
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import java.io.File


@Injectable(Scope.BY_APP)
class Directory(context:Context){

    private val publicDir = File(Environment.getExternalStorageDirectory(), "Base Camp")

    private val privateDir = context.filesDir

    private val cacheDir = context.externalCacheDir ?: context.cacheDir


    //Carpeta on es guarden
    val mediaDir:File
        get() { return File(publicDir, "BaseCamp Media").apply { mkdirs() } }

    //Carpeta on es guarden les rutes exportades
    val exportedDir:File
        get() { return publicDir.apply { mkdirs() } }

    //Carpeta on es guardaven les rutes antigues
    val legacyDir = File(privateDir, "Mine")

    //Carpeta on es guarden els fitxers de ruta
    val pointsDir : File
        get() { return File(privateDir, "ttk").apply { mkdirs() } }

    //Carpeta temporal per guardar els fitxers de punts de les rutes temporals
    val pointsTmpDir : File
        get() { return File(cacheDir, "ttk").apply { mkdirs() } }

    //Carpeta temporal que fa servir retrofit per guardar els fitxers que necesiti
    val tmpDir : File
        get() { return cacheDir.apply { mkdirs() } }


    fun isTmp(file:File):Boolean =  file.absolutePath.startsWith(cacheDir.absolutePath)

}