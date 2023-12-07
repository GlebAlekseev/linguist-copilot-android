package pro.linguistcopilot.features.reader.presentation.fragment.controller

import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

class ReaderViewController @Inject constructor(
    private val viewLifecycleOwner: LifecycleOwner,
): LifecycleOwner by viewLifecycleOwner {
    fun setupViews(){

    }
}