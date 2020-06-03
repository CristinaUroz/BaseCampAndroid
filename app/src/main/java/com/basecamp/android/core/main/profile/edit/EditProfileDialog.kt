package com.basecamp.android.core.main.profile.edit

import android.os.Bundle
import android.view.View
import android.widget.*
import cc.popkorn.inject
import com.basecamp.android.R
import com.basecamp.android.core.Dialog
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.extensions.closeFragment
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.reflect.KClass

class EditProfileDialog : Dialog<EditProfilePresenter>(), EditProfileContract.View, EditProfileContract.Router {

    private val saveButton by lazy { findViewById<Button>(R.id.dialog_editprofile_save_button) }
    private val coverPicture by lazy { findViewById<ImageView>(R.id.dialog_editprofile_cover_picture) }
    private val nameField by lazy { findViewById<TextInputEditText>(R.id.dialog_editprofile_name_field) }
    private val nameLayout by lazy { findViewById<TextInputLayout>(R.id.dialog_editprofile_name_layout) }
    private val aliasField by lazy { findViewById<TextInputEditText>(R.id.dialog_editprofile_alias_field) }
    private val aliasLayout by lazy { findViewById<TextInputLayout>(R.id.dialog_editprofile_alias_layout) }
    private val descriptionField by lazy { findViewById<TextInputEditText>(R.id.dialog_editprofile_description_field) }
    private val addPictureButton by lazy { findViewById<ImageView>(R.id.dialog_editprofile_add_cover_picture) }
    private val deletePictureButton by lazy { findViewById<ImageView>(R.id.dialog_editprofile_delete_cover_picture) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.dialog_edit_profile_progressbar) }
    private val saveProgressBar by lazy { findViewById<ProgressBar>(R.id.dialog_editprofile_button_progressbar) }
    private val errorLayout by lazy { findViewById<LinearLayout>(R.id.dialog_editprofile_error) }
    private var picture: String? = null

    var onAddPictureClick: (bundle: Bundle) -> Unit = {}
    private var cameraGalleryBundle: Bundle = Bundle.EMPTY

    private var settingsPreferences = inject<SettingsPreferences>()
    override fun getLayout(): Int = R.layout.dialog_editprofile

    override fun getPresenter(): KClass<EditProfilePresenter> = EditProfilePresenter::class

    override fun init() {
        saveButton.setOnClickListener { save() }
        deletePictureButton.setOnClickListener { deletePicture() }
        addPictureButton.setOnClickListener { onAddPictureClick.invoke(cameraGalleryBundle) }
        if (settingsPreferences.getDarkMode()) {
            aliasLayout.visibility = View.VISIBLE
            nameLayout.visibility = View.GONE
        }
    }

    private fun save() {
        if (!settingsPreferences.getDarkMode()) {
            nameField.text?.toString()?.let {
                saveProgressBar.visibility = View.VISIBLE
                notify { onSaveClick(picture, it, descriptionField.text?.toString()) }
            } ?: Toast.makeText(context, context?.getString(R.string.you_need_a_name), Toast.LENGTH_LONG).show()
        } else {
            aliasField.text?.toString()?.let {
                saveProgressBar.visibility = View.VISIBLE
                notify { onSaveClick(picture, it, descriptionField.text?.toString()) }
            } ?: Toast.makeText(context, context?.getString(R.string.you_need_an_alias), Toast.LENGTH_LONG).show()
        }
    }

    override fun showProgressBar(b: Boolean) {
        progressBar.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun showError(b: Boolean) {
        errorLayout.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun setInformation(picture: String?, name: String?, description: String?) {
        picture?.let { addPicture(it) }
        name?.let { if (!settingsPreferences.getDarkMode()) nameField.setText(it) else aliasField.setText(it) }
        description?.let { descriptionField.setText(description) }
    }

    private fun addPicture(picture: String) {
        this.picture = picture
        context?.let {
            BCGlide(it)
                .load(picture)
                .into(coverPicture)
        }
        coverPicture.visibility = View.VISIBLE
        addPictureButton.visibility = View.GONE
        deletePictureButton.visibility = View.VISIBLE
    }

    private fun deletePicture() {
        this.picture = null
        coverPicture.visibility = View.GONE
        addPictureButton.visibility = View.VISIBLE
        deletePictureButton.visibility = View.GONE
    }

    override fun closeDialog() {
        closeFragment()
    }
}