package com.example.smack.mvprefactor.base

interface MvpInteractor {
    fun isUserLoggedIn(): Boolean
    fun performUserLogOut()
}