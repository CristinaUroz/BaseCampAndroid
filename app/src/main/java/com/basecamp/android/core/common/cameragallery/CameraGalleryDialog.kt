package com.basecamp.android.core.common.cameragallery


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import cc.popkorn.inject
import com.basecamp.android.Constants
import com.basecamp.android.R
import com.basecamp.android.core.Dialog
import com.basecamp.android.core.common.PathUtil
import com.basecamp.android.core.common.extensions.closeFragment
import com.basecamp.android.data.Directory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.reflect.KClass


class CameraGalleryDialog : Dialog<CameraGalleryPresenter>(), CameraGalleryContract.View,
    CameraGalleryContract.Router {

    private val title by lazy { findViewById<TextView>(R.id.dialog_confirmation_title) }
    private val message by lazy { findViewById<TextView>(R.id.dialog_confirmation_message) }
    private val optCamera by lazy { findViewById<TextView>(R.id.dialog_confirmation_opt1) }
    private val optGallery by lazy { findViewById<TextView>(R.id.dialog_confirmation_opt2) }
    private val cancel by lazy { findViewById<TextView>(R.id.dialog_confirmation_cancel) }
    private val goBackButton by lazy { findViewById<ImageButton>(R.id.dialog_confirmation_goback_button) }
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.dialog_confirmation_constraint_layout) }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 0
        const val REQUEST_IMAGE_GALLERY = 1
    }

    private val directory = inject<Directory>()

    private var pictureFile: File? = null
    private var intentDone = false

    override fun getLayout(): Int = R.layout.dialog_confirmation

    override fun getPresenter(): KClass<CameraGalleryPresenter> = CameraGalleryPresenter::class

    override fun init() {
        if (context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
            optCamera.visibility = View.GONE
        }
        optCamera.setOnClickListener {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_IMAGE_CAPTURE
            )
        }
        optGallery.setOnClickListener {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_IMAGE_GALLERY
            )
        }
        goBackButton.setOnClickListener{
            closeFragment()
        }
        cancel.text = context?.getString(android.R.string.cancel)
        cancel.setOnClickListener { closeFragment() }
        constraintLayout.setOnClickListener { closeFragment() }
        title.text = context?.getString(R.string.add_picture)
        optCamera.text = context?.getString(R.string.camera)
        optGallery.text = context?.getString(R.string.gallery)
        message.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    pictureFile = createImageFile()
                    context?.let { ctx ->
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                                pictureFile?.also {
                                    val photoURI: Uri = FileProvider.getUriForFile(
                                        ctx,
                                        "${ctx.packageName}.fileprovider",
                                        it
                                    )
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                                }
                            }
                        }
                    }

                } else {
                    if (!grantResults.map {
                            shouldShowRequestPermissionRationale(
                                permissions[grantResults.indexOf(
                                    it
                                )]
                            )
                        }.contains(true)) {
                        close()
                        Toast.makeText(
                            context,
                            context?.getString(R.string.you_dont_have_camera_permissions),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        close()
                    }
                }
            }
            REQUEST_IMAGE_GALLERY -> {
                if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    val intentGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intentGallery.type = "image/*"
                    startActivityForResult(intentGallery, REQUEST_IMAGE_GALLERY)
                } else {
                    if (!grantResults.map {
                            shouldShowRequestPermissionRationale(
                                permissions[grantResults.indexOf(
                                    it
                                )]
                            )
                        }.contains(true)) {
                        close()
                        Toast.makeText(
                            context,
                            context?.getString(R.string.you_dont_have_camera_permissions),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        close()
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imagePath: String? = null
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    intentDone = true
                    createResizedImageFile()?.also {
                        pictureFile?.copyImageToCacheDir()?.also { original ->
                            if (original.path.resizePic(it.path)) {
                                imagePath = it.path
                            }
                        }
                    }
                } else {
                    pictureFile?.delete()
                }
            }
            REQUEST_IMAGE_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    intentDone = true
                    data?.data?.copyImageToCacheDir()?.also { file ->
                        val path = context?.let { PathUtil.getPath(it, file.toUri()) }
                        path?.let { originalPath ->
                            createResizedImageFile()?.also {
                                if (originalPath.resizePic(it.path)) {
                                    imagePath = it.path
                                }
                            }
                        }
                    }
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
        if (imagePath == null && intentDone) Toast.makeText(
            context,
            context?.getString(R.string.could_not_load_your_picture),
            Toast.LENGTH_LONG
        ).show()
        close(Bundle().apply { imagePath?.let { putString(Constants.IMAGE_PATH, it) } })

    }


    private fun Uri.copyImageToCacheDir(): File? {
        return context?.contentResolver?.openFileDescriptor(this, "r", null)?.let {
            val inputStream = FileInputStream(it.fileDescriptor)
            val file = File(context?.cacheDir, "img_${Date().time}")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            file
        }
    }

    private fun File.copyImageToCacheDir(): File? = this.toUri().copyImageToCacheDir()

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", directory.mediaDir.apply { mkdirs() })
    }


    private fun createResizedImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", directory.tmpDir)
    }

    private fun rotatePicture(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun String.resizePic(savedPhotoPath: String): Boolean {
        // Get the dimensions of the View
        val maxAcceptableSize = 1024
        val dest = File(this)
        val fis: FileInputStream

        val ei = ExifInterface(this)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val scaleFactor = BitmapFactory.Options()
            .apply { inJustDecodeBounds = true }
            .also { BitmapFactory.decodeFile(this, it) }
            .let {
                it.outWidth.toFloat().div(maxAcceptableSize)
                    .coerceAtLeast(it.outHeight.toFloat().div(maxAcceptableSize))
            }
            .let { ceil(it.toDouble()).toInt() }
            .takeIf { it > 1 }
            ?: 1

        val bitmap = BitmapFactory.Options()
            .apply { inSampleSize = scaleFactor }
            .let {

                fis = FileInputStream(dest)
                BitmapFactory.decodeStream(fis, null, it)
            }

        bitmap?.let {
            val btmp = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotatePicture(it, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotatePicture(it, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotatePicture(it, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            FileOutputStream(savedPhotoPath)
                .use { fileOutputStream ->
                    btmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                }
            return true
        } ?: return false
    }
}