package org.lwr.edgar

import android.app.Application
import com.sendbird.android.SendBird
import org.lwr.edgar.utils.PreferenceUtils



class MainApplication : Application() {

    private val APP_ID = "0F630D9E-5FAC-47E3-AC70-E5BC29618273" // US-1 Demo
    val VERSION = "3.0.40"

    override fun onCreate() {
        super.onCreate()

        PreferenceUtils.init(applicationContext)

        SendBird.init(APP_ID, applicationContext)
    }

}
