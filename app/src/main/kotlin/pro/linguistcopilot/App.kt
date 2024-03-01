package pro.linguistcopilot

import android.app.Application
import pro.linguistcopilot.di.AppComponent
import pro.linguistcopilot.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}