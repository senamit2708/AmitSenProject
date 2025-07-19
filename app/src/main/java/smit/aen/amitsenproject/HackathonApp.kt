package smit.aen.amitsenproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HackathonApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}