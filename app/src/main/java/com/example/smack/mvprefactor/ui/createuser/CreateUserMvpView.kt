package com.example.smack.mvprefactor.ui.createuser

import android.graphics.Color
import com.example.smack.mvprefactor.base.MvpView

interface CreateUserMvpView: MvpView {

    fun showToast(message: String)
    fun enableSpinner(enable: Boolean)

    fun setAvatarBackground(color: Int)
    fun setAvatarImage(id: String)
    fun sendUserDataChange()
    fun finishActivity()
}