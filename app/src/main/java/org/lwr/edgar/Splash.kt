package org.lwr.edgar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 3000
    private var Session: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        Session = getSharedPreferences("datos", Context.MODE_PRIVATE)

        Handler().postDelayed(Runnable {

            if (Session!!.getString("username","").toString().equals("")) {
                val selectStation = Intent(this@Splash, OpcionesLogin::class.java)
                startActivity(selectStation)
            } else {
                val selectStation = Intent(this@Splash, HomeMain::class.java)
                startActivity(selectStation)
            }

            finishAffinity()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
