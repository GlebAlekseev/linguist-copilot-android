package pro.linguistcopilot.features.reader.presentation.fragment.controller

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import pro.linguistcopilot.features.reader.databinding.FragmentReaderBinding
import pro.linguistcopilot.features.reader.di.scope.ReaderViewComponentScope
import pro.linguistcopilot.features.reader.presentation.fragment.ReaderFragment
import pro.linguistcopilot.features.reader.presentation.viewmodel.ReaderViewModel
import javax.inject.Inject

@ReaderViewComponentScope
class ReaderViewController @Inject constructor(
    private val fragment: ReaderFragment,
    private val context: Context,
    private val rootView: View,
    private val binding: FragmentReaderBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val readerViewModel: ReaderViewModel,
) : LifecycleOwner by viewLifecycleOwner {
    fun viewCreated() {

    }
}