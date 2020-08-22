package com.example.smack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view: View) {
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this, email, password) { loginSuccess ->
            if (loginSuccess) {
                AuthService.findUserByEmail(this) { findSuccess ->
                    if (findSuccess) {
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Something is wrong! Please re-check!",Toast.LENGTH_LONG).show()
            }
        }
    }
}