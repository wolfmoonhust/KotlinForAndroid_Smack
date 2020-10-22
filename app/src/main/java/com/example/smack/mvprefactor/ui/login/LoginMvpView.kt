package com.example.smack.mvprefactor.ui.login

import android.view.View
import com.example.smack.mvprefactor.base.MvpView

interface LoginMvpView:MvpView {
    fun loginCreateUserBtnCLicked()

    fun showToast(message: String)
    fun hideKeyboard()
    fun enableSpinner(enable: Boolean)
    fun sendDataChange()
    fun finishActivity()
}