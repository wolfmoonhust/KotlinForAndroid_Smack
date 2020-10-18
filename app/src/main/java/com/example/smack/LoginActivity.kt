package com.example.smack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE

        loginCreateUserBtn.setOnClickListener { loginCreateUserBtnClicked(it) }
        loginLoginBtn.setOnClickListener { loginLoginBtnClicked(it) }
    }

    private fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    private fun loginLoginBtnClicked(view: View) {
        enableSpinner(true)
        hideKeyboard()
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            showErorToast()
                        }
                    }
                } else {
                    showErorToast()
                }
            }
        } else {
            Toast.makeText(this, "Please fill data", Toast.LENGTH_LONG).show()
        }

    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }

    private fun showErorToast() {
        Toast.makeText(this, "Something is wrong! Please re-check!", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
}