package com.example.smack.services

import android.graphics.Color
import com.example.smack.controller.App
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    override fun toString(): String {
        return "name: $name avatarName: $avatarName avatarColor $avatarColor"
    }

    fun returnAvatarColor(components: String): Int {
        val stripperColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        var r = 0
        var g = 0
        var b = 0
        val scanner = Scanner(stripperColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
    }
}