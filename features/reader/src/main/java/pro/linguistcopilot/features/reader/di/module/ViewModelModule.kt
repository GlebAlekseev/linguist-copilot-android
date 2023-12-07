package pro.linguistcopilot.features.reader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pro.linguistcopilot.core.utils.di.ViewModelFactory
import pro.linguistcopilot.core.utils.di.ViewModelKey
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel
import pro.linguistcopilot.features.reader.di.scope.ReaderComponentScope

@Module
interface ViewModelModule {

    @ReaderComponentScope
    @Binds
    @[IntoMap ViewModelKey(ReaderViewModel::class)]
    fun bindReaderViewModel(readerViewModel: ReaderViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}