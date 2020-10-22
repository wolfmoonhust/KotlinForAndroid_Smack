package com.example.smack.mvprefactor.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.mvprefactor.base.BaseActivity
import com.example.smack.mvprefactor.ui.createuser.CreateUserMvpActivity
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smack.utilities.DEBUG
import com.example.smack.utilities.PRE_FIX
import kotlinx.android.synthetic.main.activity_login.*

class LoginMvpActivity : BaseActivity(), LoginMvpView {

    lateinit var mPresenter: LoginMvpPresenter<LoginMvpView>
    val LOG_TAG = if (DEBUG) PRE_FIX + javaClass.simpleName else javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        mPresenter = LoginPresenter()
        mPresenter.onAttach(this)
    }

    override fun initView() {
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
        loginCreateUserBtn.setOnClickListener { mPresenter.createUserBtnClicked() }
        loginLoginBtn.setOnClickListener {
            mPresenter.loginBtnClicked(
                loginEmailText.text.toString(),
                loginPasswordText.text.toString()
            )
        }
    }


    override fun onDestroy() {
        mPresenter.onDetach()
        super.onDestroy()
    }

    override fun loginCreateUserBtnCLicked() {
        val createUserIntent = Intent(this, CreateUserMvpActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    override fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    override fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


    override fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    override fun sendDataChange() {
        val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
        Log.d(LOG_TAG, "sendDataChange")
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(userDataChange)
    }

    override fun finishActivity() {
        finish()
    }


}