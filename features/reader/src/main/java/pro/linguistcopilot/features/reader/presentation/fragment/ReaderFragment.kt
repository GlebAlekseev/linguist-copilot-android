package pro.linguistcopilot.features.reader.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import pro.linguistcopilot.core.utils.FragmentWithBinding
import pro.linguistcopilot.core.utils.ILogger
import pro.linguistcopilot.core.utils.di.findDependencies
import pro.linguistcopilot.features.reader.core.BookReader
import pro.linguistcopilot.features.reader.databinding.FragmentReaderBinding
import pro.linguistcopilot.features.reader.di.DaggerReaderComponent
import pro.linguistcopilot.features.reader.di.ReaderViewSubcomponent
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import pro.linguistcopilot.features.reader.presentation.view.ReadBook
import pro.linguistcopilot.features.reader.presentation.view.delegate.NoAnimPageDelegate
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel
import pro.linguistcopilot.navigation.navigationData
import javax.inject.Inject

class ReaderFragment : FragmentWithBinding<FragmentReaderBinding>(FragmentReaderBinding::inflate) {
    private lateinit var readerViewModel: ReaderViewModel
    private var fragmentViewComponent: ReaderViewSubcomponent? = null

    @Inject
    lateinit var logger: ILogger

    @Inject
    lateinit var bookReader: BookReader

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
        fragmentViewComponent = readerViewModel.readerComponent!!
            .readerViewSubcomponentBuilder()
            .rootView(requireView())
            .binding(binding)
            .navController(findNavController())
            .readerViewModel(readerViewModel)
            .lifecycleOwner(viewLifecycleOwner)
            .fragmentManager(parentFragmentManager)
            .readerFragment(this)
            .context(requireContext())
            .build()
        super.onViewCreated(view, savedInstanceState)
        fragmentViewComponent!!.viewController.viewCreated()

        binding.readView.readBook = ReadBook(bookReader)
        binding.readView.pageDelegate = NoAnimPageDelegate(binding.readView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
    }
}