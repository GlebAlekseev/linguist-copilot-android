package pro.linguistcopilot.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pro.linguistcopilot.MainActivity
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.di.module.RootModule
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.root.DefaultRootComponent


@AppComponentScope
@Component(
    modules = [RootModule::class]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    val rootComponentFactory: DefaultRootComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}