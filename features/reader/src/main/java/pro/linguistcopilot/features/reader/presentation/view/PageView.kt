package pro.linguistcopilot.features.reader.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import pro.linguistcopilot.features.reader.databinding.ViewBookPageBinding
import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage


class PageView(context: Context) : FrameLayout(context) {
    private val binding = ViewBookPageBinding.inflate(LayoutInflater.from(context), this, true)
    var readBook: ReadBook? = null
        set(value) {
            field = value
            binding.contentTextView.readBook = value
        }
    var pageFactory: PageFactory<TextPage>? = null
        set(value) {
            field = value
            binding.contentTextView.pageFactory = value
        }

    fun resetPageOffset() {
        binding.contentTextView.resetPageOffset()
    }

    fun setContent(textPage: TextPage, resetPageOffset: Boolean = true) {
        if (resetPageOffset) {
            resetPageOffset()
        }
        binding.contentTextView.setContent(textPage)
    }

    fun markAsMainView() {
        binding.contentTextView.isMainView = true
    }
}