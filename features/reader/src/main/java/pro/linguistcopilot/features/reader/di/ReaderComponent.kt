package pro.linguistcopilot.features.reader.di

import dagger.BindsInstance
import dagger.Component
import pro.linguistcopilot.features.reader.di.scope.ReaderComponentScope
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import pro.linguistcopilot.features.reader.presentation.fragment.ReaderFragment

@ReaderComponentScope
@Component(
    dependencies = [ReaderDependencies::class],
    modules = []
)
interface ReaderComponent {
    fun inject(fragment: ReaderFragment)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bookUrlArg(bookUrlArg: BookUrlArg): Builder

        fun dependencies(dependencies: ReaderDependencies): Builder
        fun build(): ReaderComponent
    }
}