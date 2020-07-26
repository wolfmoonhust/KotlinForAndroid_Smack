package com.example.smack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)

    }


    fun loginBtnNavClicked(view: View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        Log.d("phongtx", "loginBtnNavHeaderClicked")
        startActivity(loginIntent)
    }

    fun addChannelBtnClicked(view: View) {

    }

    fun sendMessageBtnClicked(view: View) {

    }
}