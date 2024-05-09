package com.eagletech.happynote.screen.acts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eagletech.happynote.databinding.ActivityMainBinding
import com.eagletech.happynote.datauser.DataSharedPreferences
import com.eagletech.happynote.screen.frag.InsertDataFragment

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
        Log.d("data state user", (dataSharedPreferences.getCurrentUserId() != null).toString())
        Log.d("data state isPremium", dataSharedPreferences.isPremium.toString())
        if (dataSharedPreferences.isPremium == true){
            mainBinding.tvEnableApp.visibility = View.GONE
            mainBinding.fragmentContainer.visibility = View.VISIBLE
        }else{
            if ((dataSharedPreferences.getLives() > 0)) {
                mainBinding.tvEnableApp.visibility = View.GONE
                mainBinding.fragmentContainer.visibility = View.VISIBLE
            } else {
                mainBinding.tvEnableApp.visibility = View.VISIBLE
                mainBinding.fragmentContainer.visibility = View.GONE
            }
        }





    }

    private fun setClickItems() {
        mainBinding.topBar.infoIcon.setOnClickListener {
            showInfoDialog()
        }
        mainBinding.topBar.menuIcon.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    // Show dialog cho dữ liệu SharePreferences
    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        if (dataSharedPreferences.isPremium == true){
            dialog.setMessage("You have successfully registered for the package")
        }else{
            dialog.setMessage("Great, you have ${dataSharedPreferences.getLives()} turns left")
        }
        dialog.show()
    }

    override fun onRestart() {
        super.onRestart()
        checkUI()
    }

}