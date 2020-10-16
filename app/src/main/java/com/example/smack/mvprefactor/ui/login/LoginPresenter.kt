package com.example.smack.mvprefactor.ui.login

import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.mvprefactor.base.MvpPresenter

class LoginPresenter<V:LoginMvpView>: BasePresenter<V>(), LoginMvpPresenter<V>{


    override fun createUserBtnClicked() {
        TODO("Not yet implemented")
    }

    override fun loginBtnClicked() {
        TODO("Not yet implemented")
    }

}