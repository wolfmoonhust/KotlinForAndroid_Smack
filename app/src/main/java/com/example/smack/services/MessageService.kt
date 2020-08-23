package com.example.smack.services

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Channel
import com.example.smack.controller.App
import com.example.smack.utilities.URL_GET_CHANNEL

object MessageService {

    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {
        channels.clear()
        val channelRequest = object :
            JsonArrayRequest(Method.GET, URL_GET_CHANNEL, null, Response.Listener { response ->
                try {
                    for (x in 0 until response.length()) {
                        val channel = response.getJSONObject(x)
                        val channelName = channel.getString("name")
                        val channelDescrip = channel.getString("description")
                        val channelId = channel.getString("_id")
                        val newChannel = Channel(channelName, channelDescrip, channelId)

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
}