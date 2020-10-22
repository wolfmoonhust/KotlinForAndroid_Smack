package com.example.smack.mvprefactor.ui.main

import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.mvprefactor.base.MvpPresenter

interface MainMvpPresenter<V: MainMvpView>: MvpPresenter<V> {
    fun sendMessage(message: String)
    fun addChannel(channelName: String, channelDescrip: String)
    fun updateWithChannel()
    fun loginBtnNavClicked()
    fun userDataChanged()
    fun addChannelClicked()

    fun channelListItemClicked( position: Int)

}