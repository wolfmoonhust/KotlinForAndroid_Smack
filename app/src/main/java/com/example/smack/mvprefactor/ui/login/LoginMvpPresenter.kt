package com.example.smack.mvprefactor.ui.login

import android.graphics.Color
import com.example.smack.mvprefactor.base.MvpPresenter

interface LoginMvpPresenter<V:LoginMvpView>: MvpPresenter<V> {
    fun createUserBtnClicked()
    fun loginBtnClicked(email: String, password: String)


}