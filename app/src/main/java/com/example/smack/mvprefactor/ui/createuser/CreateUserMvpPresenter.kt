package com.example.smack.mvprefactor.ui.createuser

import android.graphics.Color
import com.example.smack.mvprefactor.base.MvpPresenter

interface CreateUserMvpPresenter<V: CreateUserMvpView>: MvpPresenter<V> {
    fun generateUserAvatar()
    fun generateBackgroundColor()
    fun createUser(userName: String, email: String,password: String)

}