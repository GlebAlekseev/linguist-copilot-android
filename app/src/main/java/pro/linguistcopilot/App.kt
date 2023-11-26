package pro.linguistcopilot

import android.app.Application
import pro.linguistcopilot.core.utils.di.DepsMap
import pro.linguistcopilot.core.utils.di.HasDependencies
import pro.linguistcopilot.di.AppComponent
import pro.linguistcopilot.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasDependencies {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    override lateinit var depsMap: DepsMap

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}