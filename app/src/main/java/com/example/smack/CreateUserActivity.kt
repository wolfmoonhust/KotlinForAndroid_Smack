package com.example.smack

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smack.services.AuthService
import com.example.smack.services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*


class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun generateUserAvatarClicked(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateBackgroundColorClicked(view: View) {
        val random = Random()

        val r = random.nextInt(255)
        val b = random.nextInt(255)
        val g = random.nextInt(255)
        createAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"


    }

    fun createUserClicked(view: View) {
        val userName = createUsernameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        AuthService.registerUser(this, email, password) { registerSuccess ->
            if (registerSuccess) {
                AuthService.loginUser(this, email, password) { loginSuccess ->
                    if (loginSuccess) {
                        println(AuthService.authToken)
                        println(AuthService.userEmail)
                        AuthService.createUser(
                            this,
                            userName,
                            email,
                            userAvatar,
                            avatarColor
                        ) { createSuccess ->
                            if (createSuccess){
                                println(UserDataService.toString())
                                finish()
                            }

                        }
                    }
                }
            }

        }
    }
}