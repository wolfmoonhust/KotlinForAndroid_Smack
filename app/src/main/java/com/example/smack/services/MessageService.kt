package com.example.smack.services

import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.smack.controller.App
import com.example.smack.model.Channel
import com.example.smack.model.Message
import com.example.smack.utilities.URL_GET_CHANNEL
import com.example.smack.utilities.URL_GET_MESSAGE

object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        channels.clear()
        val channelRequest = object :
            JsonArrayRequest(Method.GET, URL_GET_CHANNEL, null, Response.Listener { response ->
                try {
                    for (x in 0 until response.length()) {
                        val channel = response.getJSONObject(x)
                        val channelName = channel.getString("name")
                        val channelDescrip = channel.getString("description")
                        val channelId = channel.getString("_id")
                        val newChannel = Channel(
                            channelName,
                            channelDescrip,
                            channelId
                        )

                        channels.add(newChannel)
                    }
                    complete(true)
                } catch (exception: Exception) {
                    println(exception)
                    complete(false)
                }

            }, Response.ErrorListener { error ->
                println("getChannel $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGE$channelId"
        clearMessage()
        val messageRequest =
            object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
                try {
                    for (x in 0 until response.length()) {
                        val message = response.getJSONObject(x)
                        val messageBody = message.getString("messageBody")
                        val channelId = message.getString("channelId")
                        val id = message.getString("_id")
                        val userName = message.getString("userName")
                        val userAvatar = message.getString("userAvatar")
                        val userAvatarColor = message.getString("userAvatarColor")
                        val timeStamp = message.getString("timeStamp")

                        val newMessage = Message(messageBody, userName,channelId, userAvatar,userAvatarColor,id,timeStamp)
                        this.messages.add(newMessage)
                    }
                    complete(true)
                } catch (exception: Exception) {
                    println(exception)
                    complete(false)
                }


            }, Response.ErrorListener { error ->
                println("getMessage $error")
                complete(false)

            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                    return headers
                }
            }
        App.prefs.requestQueue.add(messageRequest)
    }

    fun clearMessage(){
        messages.clear()
    }

    fun clearChannel(){
        channels.clear()
    }
}