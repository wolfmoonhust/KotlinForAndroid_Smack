package com.example.smack.mvprefactor.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smack.mvprefactor.util.CommonUtil

abstract class BaseActivity : AppCompatActivity(), MvpView {
    private var progressDialog: ProgressDialog? = null
    var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(mContext)
    }

    override fun hideProgress() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }
}