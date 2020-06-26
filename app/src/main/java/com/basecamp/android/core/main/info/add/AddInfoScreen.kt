package com.basecamp.android.core.main.info.add

import android.app.AlertDialog
import android.view.View
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.closeFragment
import com.basecamp.android.core.main.feed.add.AddNewScreenArgs
import com.basecamp.android.domain.models.Info
import com.google.android.material.textfield.TextInputEditText
import kotlin.reflect.KClass

class AddInfoScreen : Screen<AddInfoPresenter>(), AddInfoContract.View, AddInfoContract.Router {

    private val data: AddNewScreenArgs by navArgs()
    private val errorLayout by lazy { findViewById<LinearLayout>(R.id.screen_addinfo_error) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_addinfo_progressbar) }
    private val deleteButton by lazy { findViewById<ImageView>(R.id.screen_addinfo_delete) }
    private val titleField by lazy { findViewById<TextInputEditText>(R.id.screen_addinfo_title_field) }
    private val textField by lazy { findViewById<TextInputEditText>(R.id.screen_addinfo_text_field) }
    private val errorText by lazy { findViewById<TextView>(R.id.screen_addinfo_error_text) }
    private val title by lazy { findViewById<TextView>(R.id.screen_addinfo_title) }
    private val saveButton by lazy { findViewById<Button>(R.id.screen_addinfo_save_button) }
    private val saveProgressBar by lazy { findViewById<ProgressBar>(R.id.screen_addinfo_button_progressbar) }

    override fun getLayout(): Int = R.layout.screen_addinfo

    override fun getPresenter(): KClass<AddInfoPresenter> = AddInfoPresenter::class

    override fun init() {
        data.newsId?.let {
            notify { getInfo(it) }
        }
        saveButton.setOnClickListener {
            saveProgressBar.visibility = View.VISIBLE
            saveButton.isEnabled = false
            errorText.text = ""
            save()
        }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(context?.getString(R.string.delete_info))
                .setMessage(context?.getString(R.string.are_you_sure_you_want_to_delete_this_information))
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
    }

    private fun save() {
        notify { onSaveClick(titleField.text.toString(), textField.text.toString()) }
    }

    override fun showProgressBar(b: Boolean) {
        progressBar.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun showError(b: Boolean) {
        errorLayout.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun setInformation(info: Info) {
        context?.getString(R.string.editInfo)?.let { title.text = it }
        deleteButton.visibility = View.VISIBLE
        //TODO: POSAR PREVIEW
        info.title?.let { titleField.setText(it) }
        info.text?.let { textField.setText(it) }
    }

    override fun closeDialog() {
        closeFragment()
    }

    override fun setError(error: String) {
        saveProgressBar.visibility = View.GONE
        progressBar.visibility = View.GONE
        deleteButton.isEnabled = true
        saveButton.isEnabled = true
        errorText.text = error
    }
}