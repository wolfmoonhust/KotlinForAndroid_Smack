package com.example.smack.mvprefactor.ui.main

import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.mvprefactor.base.MvpPresenter

interface MainMvpPresenter<V: MainMvpView>: MvpPresenter<V> {
    fun sendMessage(message: String)
    fun addChannel(channelName: String, channelDescrip: String)
    fun updateWithChannel()
    fun updateLogin(loggedIn : Boolean)
    fun loginBtnNavClicked()
    fun userDataChanged()

}