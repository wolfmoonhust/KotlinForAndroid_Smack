package com.example.smack.mvprefactor.ui.login

import com.example.smack.mvprefactor.base.MvpPresenter

interface LoginMvpPresenter<V:LoginMvpView>: MvpPresenter<V> {
    fun createUserBtnClicked()
    fun loginBtnClicked()
}