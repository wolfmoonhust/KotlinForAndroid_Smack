package com.example.smack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.controller.App
import com.example.smack.model.Channel
import com.example.smack.model.Message
import com.example.smack.services.AuthService
import com.example.smack.services.MessageService
import com.example.smack.services.UserDataService
import com.example.smack.utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smack.utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<Channel>
    var selectedChannel: Channel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        socket.connect()
        socket.on("channelCreated", onNewChannel)
        socket.on("messageCreated", onNewMessage)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setAdapter()
        channel_list.setOnItemClickListener { adapterView, view, position, l ->
            selectedChannel = MessageService.channels[position]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }
        if (App.prefs.isLoggedIn) {
            AuthService.findUserByEmail(this) {

            }
        }
    }


    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver, IntentFilter(
                BROADCAST_USER_DATA_CHANGE
            )
        )

        super.onResume()
    }

    override fun onPause() {

        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
        super.onDestroy()

    }

    private fun setAdapter() {
        channelAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }


    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (App.prefs.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email

                val resourceId =
                    resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(
                    UserDataService.returnAvatarColor(
                        UserDataService.avatarColor
                    )
                )
                loginBtnNavHeader.text = "Logout"
                if (context != null) {
                    MessageService.getChannels() { complete ->
                        if (complete) {
                            if (MessageService.channels.count() > 0) {
                                selectedChannel = MessageService.channels[0]
                                channelAdapter.notifyDataSetChanged()
                                updateWithChannel()
                            }

                        }
                    }
                }
            }
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            val msgBody = args[0] as String
            val channelId = args[2] as String
            val userName = args[3] as String
            val userAvatar = args[4] as String
            val userAvatarColor = args[5] as String
            val id = args[6] as String
            val timeStamp = args[7] as String

            val newMessage = Message(msgBody,userName, channelId, userAvatar, userAvatarColor, id, timeStamp)
            MessageService.messages.add(newMessage)
            println("onNewMessage ${newMessage.message}")
        }
    }

    fun loginBtnNavClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            //logout
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text = "Login"
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            Log.d("phongtx", "loginBtnNavHeaderClicked")
            startActivity(loginIntent)
        }

    }

    fun updateWithChannel() {
        mainChannelName.text = "#${selectedChannel?.name}"
    }

    fun addChannelBtnClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("Add") { dialogInterface, i ->

                    val nameTextField = dialogView.findViewById<TextView>(R.id.addChannelNameText)
                    val descripTextField =
                        dialogView.findViewById<TextView>(R.id.addChannelDescriptionText)

                    val channelName = nameTextField.text.toString()
                    val channelDescrip = descripTextField.text.toString()

                    //create channel with the name and description
                    socket.emit("newChannel", channelName, channelDescrip)
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->


                }.show()
        }
    }

    fun sendMessageBtnClicked(view: View) {
        if (App.prefs.isLoggedIn && messageTextField.text.isNotEmpty() && selectedChannel != null) {
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id
            socket.emit(
                "newMessage",
                messageTextField.text.toString(),
                userId,
                channelId,
                UserDataService.name,
                UserDataService.avatarName,
                UserDataService.avatarColor
            )

            messageTextField.text.clear()
            hideKeyboard()
        }


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread {
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(
                channelName,
                channelDescription,
                channelId
            )

            MessageService.channels.add(newChannel)
            println("${newChannel.name} ${newChannel.description} ${newChannel.id}")
            channelAdapter.notifyDataSetChanged()
        }
    }
}