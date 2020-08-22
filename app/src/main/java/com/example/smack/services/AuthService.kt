package com.example.smack.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.utilities.URL_LOGIN
import com.example.smack.utilities.URL_REGISTER
import org.json.JSONObject


object AuthService {
    var isLoggedIn = false
    var userName = ""
    var userEmail = ""
    var authToken = ""
    fun registerUser(
        context: Context,
        email: String,
        password: String,
        complete: (Boolean) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()

        val registerRequests = object : StringRequest(Method.POST,
            URL_REGISTER, Response.Listener { response ->
                println(response)
                complete(true)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not register user: $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequests)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()

        val loginRequest =
            object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
                println(response)
                try {
                    //parse json object
                    userEmail = response.getString("user")
                    authToken = response.getString("token")
                    isLoggedIn = true
                    complete(true)

                } catch (execption: Exception) {
                    println(execption)
                    complete(false)
                }


            }, Response.ErrorListener { error ->
                // where we deal with our error
                Log.d("ERROR", "Could not register user: $error")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }

            }

        Volley.newRequestQueue(context).add(loginRequest)

    }

}