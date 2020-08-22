package com.example.smack.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.utilities.*
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
        jsonBody.put(ACCOUNT_EMAIL, email)
        jsonBody.put(ACCOUNT_PASSWORD, password)

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
        jsonBody.put(ACCOUNT_EMAIL, email)
        jsonBody.put(ACCOUNT_PASSWORD, password)

        val requestBody = jsonBody.toString()

        val loginRequest =
            object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
                println(response)
                try {
                    //parse json object
                    userEmail = response.getString(RESPONSE_USER)
                    authToken = response.getString(RESPONSE_TOKEN)
                    isLoggedIn = true
                    complete(true)

                } catch (execption: Exception) {
                    println(execption)
                    complete(false)
                }


            }, Response.ErrorListener { error ->
                // where we deal with our error
                Log.d("ERROR", "Could not login user: $error")
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

    fun createUser(
        context: Context,
        name: String,
        email: String,
        avatarName: String,
        avatarColor: String,
        complete: (Boolean) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put(ACCOUNT_NAME, name)
        jsonBody.put(ACCOUNT_EMAIL, email)
        jsonBody.put(ACCOUNT_AVATAR_NAME, avatarName)
        jsonBody.put(ACCOUNT_AVATAR_COLOR, avatarColor)
        val requestBody = jsonBody.toString()

        val createUserRequest =
            object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener {response ->
                println(response)
                try {
                    UserDataService.name = response.getString(ACCOUNT_NAME)
                    UserDataService.email = response.getString(ACCOUNT_EMAIL)
                    UserDataService.avatarName = response.getString(ACCOUNT_AVATAR_NAME)
                    UserDataService.avatarColor =  response.getString(ACCOUNT_AVATAR_COLOR)
                    UserDataService.id = response.getString(ACCOUNT_ID)
                    complete(true)
                }catch (exception: Exception){
                    println(exception)
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not add user: $error")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer $authToken")
                    return headers
                }
            }
        Volley.newRequestQueue(context).add(createUserRequest)

    }

}