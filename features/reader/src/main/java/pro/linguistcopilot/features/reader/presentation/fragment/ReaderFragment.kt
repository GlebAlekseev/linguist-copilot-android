package pro.linguistcopilot.features.reader.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModelProvider
import pro.linguistcopilot.core.utils.FragmentWithBinding
import pro.linguistcopilot.core.utils.ILogger
import pro.linguistcopilot.core.utils.di.findDependencies
import pro.linguistcopilot.features.reader.databinding.FragmentReaderBinding
import pro.linguistcopilot.features.reader.di.DaggerReaderComponent
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel
import pro.linguistcopilot.navigation.navigationData
import javax.inject.Inject

class ReaderFragment : FragmentWithBinding<FragmentReaderBinding>(FragmentReaderBinding::inflate) {
    private lateinit var readerViewModel: ReaderViewModel

    @Inject
    lateinit var logger: ILogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bookUrlArg = (navigationData as? BookUrlArg) ?: return

        readerViewModel =
            ViewModelProvider(this)[ReaderViewModel::class.java]
        if (readerViewModel.readerComponent == null) {
            readerViewModel.readerComponent = DaggerReaderComponent.builder()
                .dependencies(findDependencies())
                .bookUrlArg(bookUrlArg)
                .build()
                .apply {
                    inject(this@ReaderFragment)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContent {
            Text(text = "reader")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}