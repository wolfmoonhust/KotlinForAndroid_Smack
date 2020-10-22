package com.example.smack.mvprefactor.ui.main

import android.util.Log
import com.example.smack.controller.App
import com.example.smack.model.Channel
import com.example.smack.model.Message
import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.services.AuthService
import com.example.smack.services.MessageService
import com.example.smack.services.UserDataService
import com.example.smack.utilities.DEBUG
import com.example.smack.utilities.PRE_FIX
import com.example.smack.utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter

class MainPresenter<V : MainMvpView> : BasePresenter<V>(), MainMvpPresenter<V> {
    val LOG_TAG = if (DEBUG) PRE_FIX + javaClass.simpleName else javaClass.simpleName
    val socket = IO.socket(SOCKET_URL)

    init {
        val onNewMessage = Emitter.Listener { args ->
            if (App.prefs.isLoggedIn) {

                val channelId = args[2] as String
                if (channelId == selectedChannel?.id) {
                    val msgBody = args[0] as String
                    val userName = args[3] as String
                    val userAvatar = args[4] as String
                    val userAvatarColor = args[5] as String
                    val id = args[6] as String
                    val timeStamp = args[7] as String

                    val newMessage = Message(
                        msgBody,
                        userName,
                        channelId,
                        userAvatar,
                        userAvatarColor,
                        id,
                        timeStamp
                    )
                    MessageService.messages.add(newMessage)

                    getView()?.let {
                        it.updateChannelLists()
                        it.updateMessageLists()
                    }


                }
            }
        }
        val onNewChannel = Emitter.Listener { args ->
            if (App.prefs.isLoggedIn) {

                val channelName = args[0] as String
                val channelDescription = args[1] as String
                val channelId = args[2] as String

                val newChannel = Channel(
                    channelName,
                    channelDescription,
                    channelId
                )

                MessageService.channels.add(newChannel)
                getView()?.updateChannelLists()


            }
        }

        socket.connect()
        socket.on("channelCreated", onNewChannel)
        socket.on("messageCreated", onNewMessage)

        if (App.prefs.isLoggedIn) {
            AuthService.findUserByEmail() {success ->
                if (success) getView()?.sendDataChange()

            }
        }
    }


    var selectedChannel: Channel? = null

    override fun sendMessage(message: String) {
        if (message.isNotEmpty() && App.prefs.isLoggedIn && selectedChannel != null) {
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id

            socket.emit(
                "newMessage",
                message,
                userId,
                channelId,
                UserDataService.name,
                UserDataService.avatarName,
                UserDataService.avatarColor
            )
        }

        getView()?.let {
            it.hideKeyboard()
            it.clearMessageField()
        }
    }


    override fun onAttach(view: V?) {
        super.onAttach(view)
        getView()?.setAdapter(MessageService.channels, MessageService.messages)
//        if (App.prefs.isLoggedIn){
//            AuthService.findUserByEmail {
//                success ->
//                Log.d(LOG_TAG, "onAttach findUserByEmail $success")
//            }
//        }
    }

    override fun onDetach() {
        socket.disconnect()
        super.onDetach()
    }

    override fun addChannel(channelName: String, channelDescrip: String) {
        socket.emit("newChannel", channelName, channelDescrip)
    }

    override fun updateWithChannel() {
        getView()?.let {
            it.updateWithChannel(selectedChannel!!.name)
            it.closeDrawer()
        }

        if (selectedChannel != null) {
            MessageService.getMessages(selectedChannel!!.id) { complete ->
                if (complete){
                   getView()?.let {
                       it.updateNewMessagesData(MessageService.messages)
                   }
                }
            }
        }
    }

    override fun loginBtnNavClicked() {
        getView()?.let {
            if (App.prefs.isLoggedIn) {
                UserDataService.logout()
                it.logOut()
            } else {
                it.startLoginActivity()
            }
        }

    }

    override fun userDataChanged() {
        if (App.prefs.isLoggedIn) {
            getView()?.let {
                it.updateDataChange(
                    UserDataService.name,
                    UserDataService.email,
                    UserDataService.avatarName,
                    UserDataService.returnAvatarColor(UserDataService.avatarColor)
                )

            }

            MessageService.getChannels() { complete ->
                if (complete) {
                    if (MessageService.channels.count() > 0) {
                        selectedChannel = MessageService.channels[0]

                        getView()?.updateChannelLists()
                        updateWithChannel()
                    }

                }
            }
        }

    }

    override fun addChannelClicked() {
        getView()?.showChannelDialog()
    }

    override fun channelListItemClicked(position: Int) {
       selectedChannel = MessageService.channels[position]
        updateWithChannel()
    }
}