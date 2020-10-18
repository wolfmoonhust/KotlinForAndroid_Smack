package com.example.smack.mvprefactor.ui.login

import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.services.AuthService

class LoginPresenter<V : LoginMvpView> : BasePresenter<V>(), LoginMvpPresenter<V> {


    override fun createUserBtnClicked() {
        getView()?.loginCreateUserBtnCLicked()
    }

    override fun loginBtnClicked(email: String, password: String) {
        getView()?.let {
            it.hideKeyboard()
            it.enableSpinner(true)
            if (email.isNotEmpty() && email.isNotEmpty()) {
                AuthService.loginUser(email, password) { loginSuccess ->
                    if (loginSuccess) {
                        AuthService.findUserByEmail() { findSuccess ->
                            if (findSuccess) {
                                it.enableSpinner(false)
                                it.sendDataChange()
                                it.finishActivity()
                            } else {
                                it.enableSpinner(false)
                                it.showToast("Something is wrong! Please re-check!")
                            }
                        }
                    } else {
                        it.enableSpinner(false)
                        it.showToast("Something is wrong! Please re-check!")
                    }


                }
            } else {
                it.showToast("Please fill data")
            }


        }


    }



}