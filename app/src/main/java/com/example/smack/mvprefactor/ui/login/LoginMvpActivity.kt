package com.example.smack.mvprefactor.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.smack.mvprefactor.base.BaseActivity

class LoginMvpActivity:BaseActivity(), LoginMvpView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun loginCreateUserBtnCLicked(view: View?) {
        TODO("Not yet implemented")
    }

    override fun loginLoginBtnClicked(view: View?) {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String) {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show()
    }

    override fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun initView(){
        
    }
}