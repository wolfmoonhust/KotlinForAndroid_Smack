package com.example.smack.mvprefactor.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smack.R
import com.example.smack.adapter.MessageAdapter
import com.example.smack.model.Channel
import com.example.smack.model.Message
import com.example.smack.mvprefactor.base.BaseActivity
import com.example.smack.mvprefactor.ui.login.LoginMvpActivity
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smack.utilities.DEBUG
import com.example.smack.utilities.PRE_FIX
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainMvpActivity : BaseActivity(), MainMvpView {
    val LOG_TAG = if (DEBUG) PRE_FIX + javaClass.simpleName else javaClass.simpleName
    lateinit var mPresenter: MainMvpPresenter<MainMvpView>

    lateinit var channelAdapter: ArrayAdapter<Channel>
    lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        mPresenter = MainPresenter()
        mPresenter.onAttach(this)

    }

    override fun onDestroy() {
        mPresenter.onDetach()
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }


    override fun initView() {
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver, IntentFilter(
                BROADCAST_USER_DATA_CHANGE
            )
        )

        channel_list.setOnItemClickListener { _, _, position, _ ->
            mPresenter.channelListItemClicked(position)
        }

        addChannelBtn.setOnClickListener { mPresenter.addChannelClicked() }
        loginBtnNavHeader.setOnClickListener { mPresenter.loginBtnNavClicked() }


    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(LOG_TAG, "userDataChange")
            mPresenter.userDataChanged()
        }
    }

    override fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }

    override fun updateMessageLists() {
        messageAdapter.notifyDataSetChanged()
    }

    override fun updateChannelLists() {
        channelAdapter.notifyDataSetChanged()
    }

    override fun showChannelDialog() {
        val builder = AlertDialog.Builder(mContext)
        val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
        builder.setView(dialogView)
            .setPositiveButton("Add") { dialogInterface, i ->

                val nameTextField = dialogView.findViewById<TextView>(R.id.addChannelNameText)
                val descripTextField =
                    dialogView.findViewById<TextView>(R.id.addChannelDescriptionText)

                val channelName = nameTextField.text.toString()
                val channelDescrip = descripTextField.text.toString()

                //create channel with the name and description
                mPresenter.addChannel(channelName, channelDescrip)
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->

            }.show()


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setAdapter(channels: ArrayList<Channel>, messages: ArrayList<Message>) {
        channelAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, channels)
        channel_list.adapter = channelAdapter

        messageAdapter = MessageAdapter(this, messages)
        messageListView.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(this)
        messageListView.layoutManager = layoutManager
    }

    override fun updateWithChannel(mainChannel: String, complete: Boolean, itemCount: Int) {
        mainChannelName.text = mainChannel
        if (complete) {
            messageAdapter.notifyDataSetChanged()
            if (itemCount > 0) {
                messageListView.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    override fun startLoginActivity() {
        val loginIntent = Intent(this, LoginMvpActivity::class.java)
        startActivity(loginIntent)
    }

    override fun logOut() {
        channelAdapter.notifyDataSetChanged()
        messageAdapter.notifyDataSetChanged()

        userNameNavHeader.text = ""
        userEmailNavHeader.text = ""
        userImageNavHeader.setImageResource(R.drawable.profiledefault)
        userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        loginBtnNavHeader.text = "Login"
        mainChannelName.text = "Please log in"
    }

    override fun updateWithChannel(mainChannel: String) {
        mainChannelName.text = mainChannel
    }

    override fun clearMessageField() {
        messageTextField.text.clear()
    }

    override fun updateDataChange(
        name: String,
        email: String,
        avatarName: String,
        backgroundColor: Int
    ) {
        userNameNavHeader.text = name
        userEmailNavHeader.text = email
        val resourceId =
            resources.getIdentifier(avatarName, "drawable", packageName)
        userImageNavHeader.setImageResource(resourceId)
        userImageNavHeader.setBackgroundColor(backgroundColor)
        loginBtnNavHeader.text = "Logout"
    }

    override fun updateNewMessagesData(newMessage: ArrayList<Message>) {
        messageAdapter.setNewData(newMessage)
        messageAdapter.notifyDataSetChanged()
        if ( messageAdapter.itemCount > 0) {
            messageListView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    override fun updateNewChannelsData(newChannel: ArrayList<Channel>) {
        channelAdapter.clear()
        channelAdapter.addAll(newChannel)
        channelAdapter.notifyDataSetChanged()
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun sendDataChange() {
        val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
        Log.d(LOG_TAG, "sendDataChange")
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(userDataChange)
    }
}