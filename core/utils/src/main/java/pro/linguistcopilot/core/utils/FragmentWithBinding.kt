package pro.linguistcopilot.core.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class FragmentWithBinding<T : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T
) : Fragment() {
    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
