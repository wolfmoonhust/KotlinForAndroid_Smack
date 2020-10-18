package com.example.smack.mvprefactor.ui.createuser

import android.graphics.Color
import com.example.smack.mvprefactor.base.BasePresenter
import com.example.smack.services.AuthService
import java.util.*

class CreateUserPresenter<V : CreateUserMvpView> : BasePresenter<V>(), CreateUserMvpPresenter<V> {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun generateUserAvatar() {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        userAvatar = if (color == 0) {
            "light$avatar"
        } else {
            "dark$avatar"
        }
        getView()?.setAvatarImage(userAvatar)
    }



    override fun generateBackgroundColor() {
        val random = Random()
        val r = random.nextInt(255)
        val b = random.nextInt(255)
        val g = random.nextInt(255)

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarColor = "[$savedR, $savedG, $savedB, 1]"

        getView()?.setAvatarBackground(Color.rgb(r, g, b))

    }

    override fun createUser(userName: String, email: String, password: String) {

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            getView()?.showToast("Please fill in")
            return
        }
        getView()?.let {
            it.enableSpinner(true)
            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(
                                userName,
                                email,
                                userAvatar,
                                avatarColor
                            ) { createSuccess ->
                                if (createSuccess) {
                                    it.enableSpinner(false)
                                    it.sendUserDataChange()
                                    it.finishActivity()
                                } else {
                                    it.showToast("create id is fail, please try again!")
                                    it.enableSpinner(false)
                                }
                            }
                        } else {
                            it.showToast("login is fail, please try again!")
                            it.enableSpinner(false)
                        }
                    }
                } else {
                    it.showToast("Something went wrong, please try again!")
                    it.enableSpinner(false)
                }
            }
        }

    }

}