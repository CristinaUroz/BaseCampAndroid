package com.basecamp.android.core.main.feed.add

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.basecamp.android.Constants
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.cameragallery.CameraGalleryDialog.Companion.NAV_REQUEST_CODE
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.extensions.closeFragment
import com.basecamp.android.core.common.extensions.getLeaderName
import com.basecamp.android.domain.models.News
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

class AddNewScreen : Screen<AddNewPresenter>(), AddNewContract.View, AddNewContract.Router {

    private val data: AddNewScreenArgs by navArgs()
    private val errorLayout by lazy { findViewById<LinearLayout>(R.id.screen_addnew_error) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_addnew_progressbar) }
    private val deleteButton by lazy { findViewById<ImageView>(R.id.screen_addnew_delete) }
    private val coverPicture by lazy { findViewById<ImageView>(R.id.screen_addnew_cover_picture) }
    private val deletePictureButton by lazy { findViewById<ImageView>(R.id.screen_addnew_delete_cover_picture) }
    private val addPictureButton by lazy { findViewById<ImageView>(R.id.screen_addnew_add_cover_picture) }
    private val titleField by lazy { findViewById<TextInputEditText>(R.id.screen_addnew_title_field) }
    private val textField by lazy { findViewById<TextInputEditText>(R.id.screen_addnew_text_field) }
    private val dateField by lazy { findViewById<TextInputEditText>(R.id.screen_addnew_date_field) }
    private val timeField by lazy { findViewById<TextInputEditText>(R.id.screen_addnew_time_field) }
    private val authorSpinner by lazy { findViewById<Spinner>(R.id.screen_addnew_author_spinner) }
    private val errorText by lazy { findViewById<TextView>(R.id.screen_addnew_error_text) }
    private val title by lazy { findViewById<TextView>(R.id.screen_addnew_title) }
    private val saveButton by lazy { findViewById<Button>(R.id.screen_addnew_save_button) }
    private val saveProgressBar by lazy { findViewById<ProgressBar>(R.id.screen_addnew_button_progressbar) }
    private var picture: String? = null

    private var cal = Calendar.getInstance()


    private val sdf = SimpleDateFormat("dd MMMM yyy", Locale.getDefault())
    private val shf = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun getLayout(): Int = R.layout.screen_addnew

    override fun getPresenter(): KClass<AddNewPresenter> = AddNewPresenter::class

    override fun init() {
        data.newsId?.let {
            notify { getNews(it) }
        }

        context?.let { context ->
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                mutableListOf(context.getString(R.string.select_author))
                    .apply {
                        addAll((1..4).map { it.getLeaderName() })
                    })
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    authorSpinner.adapter = adapter
                }
        }

        saveButton.setOnClickListener {
            saveProgressBar.visibility = View.VISIBLE
            saveButton.isEnabled = false
            errorText.text = ""
            save()
        }
        deletePictureButton.setOnClickListener { deletePicture() }
        addPictureButton.setOnClickListener {
            navigate(AddNewScreenDirections.actionAddnewScreenToCameragalleryDialog(), NAV_REQUEST_CODE)
        }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(context?.getString(R.string.delete_news))
                .setMessage(context?.getString(R.string.are_you_sure_you_want_to_delete_these_news))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    progressBar.visibility = View.VISIBLE
                    saveButton.isEnabled = false
                    errorText.text = ""
                    notify { onDeleteClick() }
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(context?.getDrawable(R.drawable.error))
                .show()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            dateField.setText(sdf.format(cal.time))
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour,  minute->
            cal.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            timeField.setText(shf.format(cal.time))
        }

        context?.let { ctx ->
            dateField.setOnClickListener {
                DatePickerDialog(ctx, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            timeField.setOnClickListener {
                TimePickerDialog(ctx, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
            }
        }

    }

    override fun darkVersion(b: Boolean) {
        authorSpinner.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun save() {
        notify { onSaveClick(picture, titleField.text.toString(), textField.text.toString(), authorSpinner.selectedItemPosition.takeIf { it != 0 }, cal.timeInMillis) }
    }

    override fun showProgressBar(b: Boolean) {
        progressBar.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun showError(b: Boolean) {
        errorLayout.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun setInformation(news: News) {
        context?.getString(R.string.editNews)?.let { title.text = it }
        deleteButton.visibility = View.VISIBLE
        //TODO: POSAR PREVIEW
        news.picture?.takeIf { it != "" }?.let { addPicture(it) }
        news.title?.let { titleField.setText(it) }
        news.text?.let { textField.setText(it) }
        news.author?.let { authorSpinner.setSelection(it) }
        news.timestamp.let {
            dateField.setText(sdf.format(it))
            timeField.setText(shf.format(it))
            cal.timeInMillis = it
        }
    }

    private fun addPicture(picture: String) {
        this.picture = picture
        context?.let {
            BCGlide(it)
                .load(picture)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(80)))
                .into(coverPicture)
        }
        coverPicture.visibility = View.VISIBLE
        addPictureButton.visibility = View.GONE
        deletePictureButton.visibility = View.VISIBLE
    }

    private fun deletePicture() {
        this.picture = ""
        coverPicture.visibility = View.GONE
        addPictureButton.visibility = View.VISIBLE
        deletePictureButton.visibility = View.GONE
    }

    override fun closeDialog() {
        closeFragment()
    }


    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        if (requestCode == NAV_REQUEST_CODE) {
            bundle.getString(Constants.IMAGE_PATH)?.let {
                addPicture(it)
                picture = it
            }
        }
        super.onFragmentResult(requestCode, bundle)
    }

    override fun setError(error: String) {
        saveProgressBar.visibility = View.GONE
        progressBar.visibility = View.GONE
        deleteButton.isEnabled = true
        saveButton.isEnabled = true
        errorText.text = error
    }
}