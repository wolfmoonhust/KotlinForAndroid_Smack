package com.example.smack.utilities;

const val EXTRA_PLAYER = "player"
const val EXTRA_CATEGORY = "category"
const val EXTRA_PRODUCT = " product"
const val BASE_URL = "http://10.0.2.2:3005/v1/"
const val SOCKET_URL = "http://10.0.2.2:3005/"

const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER_BY_EMAIL ="${BASE_URL}user/byEmail/"
const val URL_GET_CHANNEL ="${BASE_URL}channel"
const val URL_GET_MESSAGE ="${BASE_URL}/message/byChannel/"


const val ACCOUNT_ID = "_id"
const val ACCOUNT_NAME = "name"
const val ACCOUNT_EMAIL = "email"
const val ACCOUNT_AVATAR_COLOR = "avatarColor"
const val ACCOUNT_AVATAR_NAME = "avatarName"
const val ACCOUNT_PASSWORD = "password"

const val RESPONSE_TOKEN = "token"
const val RESPONSE_USER =  "user"

//broadcast contants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"