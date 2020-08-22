package com.example.smack.services

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    override fun toString(): String {
        return "name: $name avatarName: $avatarName avatarColor $avatarColor"
    }
}