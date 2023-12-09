package pro.linguistcopilot.features.reader.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import pro.linguistcopilot.features.reader.presentation.fragment.ReaderFragment
import pro.linguistcopilot.features.reader.di.module.ViewModelModule
import pro.linguistcopilot.features.reader.di.scope.ReaderComponentScope
import pro.linguistcopilot.features.reader.domain.BookUrlArg

@ReaderComponentScope
@Component(
    dependencies = [ReaderDependencies::class],
    modules = []
)
interface ReaderComponent {
    fun inject(fragment: ReaderFragment)
    fun readerViewSubcomponentBuilder(): ReaderViewSubcomponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bookUrlArg(bookUrlArg: BookUrlArg): Builder

        fun dependencies(dependencies: ReaderDependencies): Builder
        fun build(): ReaderComponent
    }
}