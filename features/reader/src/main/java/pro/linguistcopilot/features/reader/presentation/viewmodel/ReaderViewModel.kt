package pro.linguistcopilot.features.reader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import pro.linguistcopilot.features.reader.di.ReaderComponent
import javax.inject.Inject

class ReaderViewModel @Inject constructor() : ViewModel() {
    var readerComponent: ReaderComponent? = null


    override fun onCleared() {
        super.onCleared()
        readerComponent = null
    }
}