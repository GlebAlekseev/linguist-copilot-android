package pro.linguistcopilot.features.reader.di

import dagger.Component
import pro.linguistcopilot.features.reader.presentation.fragment.ReaderFragment
import pro.linguistcopilot.features.reader.di.module.ViewModelModule
import pro.linguistcopilot.features.reader.di.scope.ReaderComponentScope

@ReaderComponentScope
@Component(
    dependencies = [ReaderDependencies::class],
    modules = [ViewModelModule::class]
)
interface ReaderComponent {
    fun inject(fragment: ReaderFragment)
    fun readerViewSubcomponentBuilder(): ReaderViewSubcomponent.Builder

    @Component.Factory
    interface Factory {
        fun create(dependencies: ReaderDependencies): ReaderComponent
    }
}