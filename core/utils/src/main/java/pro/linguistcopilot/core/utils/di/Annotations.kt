package pro.linguistcopilot.core.utils.di


import dagger.MapKey
import pro.linguistcopilot.core.utils.di.Dependencies
import java.lang.annotation.Inherited
import javax.inject.Qualifier
import kotlin.reflect.KClass

@MapKey
annotation class DependenciesKey(val value: KClass<out Dependencies>)

@Inherited
@Qualifier
annotation class ApplicationContext
