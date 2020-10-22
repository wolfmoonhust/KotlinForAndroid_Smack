package com.example.smack.mvprefactor.ui.main

import com.example.smack.model.Channel
import com.example.smack.model.Message
import com.example.smack.mvprefactor.base.MvpView

interface MainMvpView : MvpView {
    fun hideKeyboard()
    fun updateMessageLists()
    fun updateChannelLists()
    fun showChannelDialog()

    fun startLoginActivity()
    fun logOut()
    fun updateWithChannel(mainChannel: String)
    fun updateWithChannel(mainChannel: String, complete: Boolean, itemCount: Int)
    fun setAdapter(channels: ArrayList<Channel>, messages: ArrayList<Message>)

    fun clearMessageField()

    fun updateDataChange(name: String, email: String, avatarName: String, backgroundColor: Int)

    fun updateNewMessagesData(newMessage: ArrayList<Message>)
    fun updateNewChannelsData(newChannel: ArrayList<Channel>)

    fun closeDrawer()

    fun sendDataChange()
}