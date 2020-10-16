package com.example.smack.mvprefactor.base

interface MvpPresenter {
    fun isUserLoggedIn(): Boolean
    fun performUserLogOut()
}