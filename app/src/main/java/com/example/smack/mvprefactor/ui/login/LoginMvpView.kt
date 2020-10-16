package com.example.smack.mvprefactor.ui.login

import android.view.View
import com.example.smack.mvprefactor.base.MvpView

interface LoginMvpView:MvpView {
    fun loginCreateUserBtnCLicked(view: View?)
    fun loginLoginBtnClicked(view: View?)
    fun showToast(message: String)
    fun hideKeyboard()
}