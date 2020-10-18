package com.example.smack.mvprefactor.ui.createuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.mvprefactor.base.BaseActivity
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserMvpActivity : BaseActivity(), CreateUserMvpView {
    lateinit var mPresenter: CreateUserMvpPresenter<CreateUserMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        mPresenter = CreateUserPresenter<CreateUserMvpView>()
        mPresenter.onAttach(this)

    }

    override fun onDestroy() {
        mPresenter.onDetach()
        super.onDestroy()
    }

    override fun initView() {
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE

        createUserBtn.setOnClickListener {
            mPresenter.createUser(
                createUsernameText.text.toString(),
                createEmailText.text.toString(),
                createPasswordText.text.toString()
            )
        }
        createGenerateBackgroundColorBtn.setOnClickListener { mPresenter.generateBackgroundColor() }
        createAvatarImageView.setOnClickListener { mPresenter.generateUserAvatar() }
    }

    override fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    override fun enableSpinner(enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        createGenerateBackgroundColorBtn.isEnabled = !enable
    }

    override fun setAvatarBackground(color: Int) {
        createAvatarImageView.setBackgroundColor(color)
    }

    override fun setAvatarImage(id: String) {
        val resourceId = resources.getIdentifier(id, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    override fun sendUserDataChange() {
        val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
        LocalBroadcastManager.getInstance(mContext)
            .sendBroadcast(userDataChange)
    }

    override fun finishActivity() {
        finish()
    }
}