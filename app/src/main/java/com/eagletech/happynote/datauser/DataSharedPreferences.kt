package com.eagletech.happynote.datauser

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService


class DataSharedPreferences constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    companion object {
        @Volatile
        private var instance: DataSharedPreferences? = null

        fun getInstance(context: Context): DataSharedPreferences {
            return instance ?: synchronized(this) {
                instance ?: DataSharedPreferences(context).also { instance = it }
            }
        }
    }

    // Lấy ra thông tin mua theo lượt
    fun getLives(): Int {
        return sharedPreferences.getInt("lives", 0)
    }

    fun setLives(lives: Int) {
        sharedPreferences.edit().putInt("lives", lives).apply()
    }

    fun addLives(amount: Int) {
        val currentLives = getLives()
        setLives(currentLives + amount)
    }

    fun removeLife() {
        val currentLives = getLives()
        if (currentLives > 0) {
            setLives(currentLives - 1)
        }
    }

    fun isLivesDataEmpty(): Boolean {
        // Kiểm tra xem có tồn tại khóa "lives" không
        return !sharedPreferences.contains("lives")
    }

    fun isUserDataEmpty(): Boolean {
        // Kiểm tra xem có tồn tại khóa "UserId" không
        return !sharedPreferences.contains("UserId")
    }
    // Lấy thông tin mua premium
    var isPremium: Boolean?
        get() {
            val userId = sharedPreferences.getString("UserId", "")
            return sharedPreferences.getBoolean("PremiumPlan_\$userId$userId", false)
        }
        set(state) {
            val userId = sharedPreferences.getString("UserId", "")
            sharedPreferences.edit().putBoolean("PremiumPlan_\$userId$userId", state!!).apply()
//            sharedPreferences.edit().apply()
        }

    // Lưu thông tin người dùng
    fun currentUserId(userid: String?) {
        sharedPreferences.edit().putString("UserId", userid).apply()
//        sharedPreferences.edit().apply()
    }

    // Lấy ra thông tin id người dùng
    fun getCurrentUserId(): String? {
        return sharedPreferences.getString("UserId", null)
    }

}
