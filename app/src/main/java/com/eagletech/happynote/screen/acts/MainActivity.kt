package com.eagletech.happynote.screen.acts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.eagletech.happynote.databinding.ActivityMainBinding
import com.eagletech.happynote.datauser.DataSharedPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var dataSharedPreferences: DataSharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        dataSharedPreferences = DataSharedPreferences.getInstance(this)
        checkUI()
        setClickItems()

    }

    private fun checkUI() {
        Log.d("data", dataSharedPreferences.getLives().toString())
        dataSharedPreferences.currentUserId("abc")
        Log.d("data user", dataSharedPreferences.getCurrentUserId().toString())
        Log.d("data state user", (dataSharedPreferences.getCurrentUserId() != null).toString())
        if (dataSharedPreferences.getCurrentUserId() == null) {
            Log.d("data state lives", (dataSharedPreferences.getLives() > 0).toString())
            mainBinding.topBar.titleTextView.text = "Welcome, please buy usage"

        } else {
            if ((dataSharedPreferences.getLives() > 0)) {
                mainBinding.topBar.titleTextView.text =
                    "Great, you have ${dataSharedPreferences.getLives()} turns left"
                mainBinding.tvEnableApp.visibility = View.GONE
                mainBinding.fragmentContainer.visibility = View.VISIBLE
            } else {
                mainBinding.topBar.titleTextView.text = "Welcome, please buy usage"
                mainBinding.tvEnableApp.visibility = View.VISIBLE
                mainBinding.fragmentContainer.visibility = View.GONE

            }
        }



    }

    private fun setClickItems() {
        mainBinding.topBar.menuIcon.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRestart() {
        super.onRestart()
        checkUI()
    }
}