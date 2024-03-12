package pro.linguistcopilot

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.di.Dependencies
import pro.linguistcopilot.di.module.RoomModule
import pro.linguistcopilot.di.module.RootModule
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.root.DefaultRootComponent


@AppComponentScope
@Component(
    modules = [RootModule::class, RoomModule::class]
)
interface AppComponent : Dependencies {
    fun inject(mainActivity: MainActivity)

    val rootComponentFactory: DefaultRootComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}