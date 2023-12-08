package pro.linguistcopilot.features.reader.di

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import dagger.BindsInstance
import dagger.Subcomponent
import pro.linguistcopilot.features.reader.databinding.FragmentReaderBinding
import pro.linguistcopilot.features.reader.di.scope.ReaderViewComponentScope
import pro.linguistcopilot.features.reader.presentation.fragment.ReaderFragment
import pro.linguistcopilot.features.reader.presentation.fragment.controller.ReaderViewController
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel


@ReaderViewComponentScope
@Subcomponent(modules = [])
interface ReaderViewSubcomponent {
    fun inject(fragment: ReaderFragment)
    val viewController: ReaderViewController

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun rootView(rootView: View): Builder

        @BindsInstance
        fun binding(binding: FragmentReaderBinding): Builder

        @BindsInstance
        fun lifecycleOwner(lifecycleOwner: LifecycleOwner): Builder

        @BindsInstance
        fun readerViewModel(readerViewModel: ReaderViewModel): Builder

        @BindsInstance
        fun navController(navController: NavController): Builder

        @BindsInstance
        fun fragmentManager(fragmentManager: FragmentManager): Builder

        @BindsInstance
        fun readerFragment(fragment: ReaderFragment): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ReaderViewSubcomponent
    }
}