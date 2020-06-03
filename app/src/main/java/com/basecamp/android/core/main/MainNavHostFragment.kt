package com.basecamp.android.core.main

import androidx.fragment.app.Fragment
import com.basecamp.android.R
import com.basecamp.android.core.common.BaseNavHostFragment
import com.basecamp.android.core.main.profile.ProfileScreen
import com.basecamp.android.core.main.profile.edit.EditProfileDialog

/**
 * Manages all "Main" navigation flows.
 */
@Suppress("unused")
class MainNavHostFragment : BaseNavHostFragment() {

    override fun applyNavigationRoutes(originFragment: Fragment) {
        when (originFragment) {
            is ProfileScreen -> {
                originFragment.onEditClick = {
                    navigate(R.id.action_profile_screen_to_editprofile_dialog)
                }
            }
            is EditProfileDialog -> {
                originFragment.onAddPictureClick = { bundle ->
                    navigate(R.id.action_editprofile_dialog_to_cameragallery_dialog, bundle)
                }
            }
        }
    }
}