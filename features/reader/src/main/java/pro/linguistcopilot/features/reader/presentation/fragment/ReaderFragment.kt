package pro.linguistcopilot.features.reader.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import pro.linguistcopilot.core.utils.FragmentWithBinding
import pro.linguistcopilot.core.utils.di.findDependencies
import pro.linguistcopilot.features.reader.R
import pro.linguistcopilot.features.reader.databinding.FragmentReaderBinding
import pro.linguistcopilot.features.reader.di.DaggerReaderComponent
import pro.linguistcopilot.features.reader.di.ReaderComponent
import pro.linguistcopilot.features.reader.di.ReaderViewSubcomponent
import pro.linguistcopilot.features.reader.domain.Book
import pro.linguistcopilot.features.reader.domain.Book.Companion.bookInfo
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel
import pro.linguistcopilot.navigation.navigationData
import javax.inject.Inject

class ReaderFragment : FragmentWithBinding<FragmentReaderBinding>(FragmentReaderBinding::inflate) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var readerViewModel: ReaderViewModel
    private var fragmentComponent: ReaderComponent? = null
    private var fragmentViewComponent: ReaderViewSubcomponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent = DaggerReaderComponent.factory()
            .create(findDependencies())
            .apply {
                inject(this@ReaderFragment)
            }
        readerViewModel =
            ViewModelProvider(this, viewModelFactory)[ReaderViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookUrlArg = (navigationData as? BookUrlArg) ?: return
        val book = Book(
            url = bookUrlArg.bookUrl,
            bookType = Book.EPUB,
            createdAt = System.currentTimeMillis(),
            changedAt = System.currentTimeMillis()
        )
        val bookInfo = book.bookInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent = fragmentComponent!!
            .readerViewSubcomponentBuilder()
            .rootView(requireView())
            .binding(binding)
            .navController(findNavController())
            .readerViewModel(readerViewModel)
            .fragmentManager(parentFragmentManager)
            .lifecycleOwner(viewLifecycleOwner)
            .todoItemsFragment(this)
            .context(requireContext())
            .build()
        super.onViewCreated(view, savedInstanceState)
        fragmentViewComponent!!.viewController.setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
    }
}