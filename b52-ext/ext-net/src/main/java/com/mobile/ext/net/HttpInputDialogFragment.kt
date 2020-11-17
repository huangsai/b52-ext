package com.mobile.ext.net

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mobile.ext.net.databinding.FragmentHttpInputDialogBinding
import com.mobile.guava.android.mvvm.BaseAppCompatDialogFragment

/**
 * 动态切换网络环境
 */
class HttpInputDialogFragment(private var callback: OnDialogDismissListener?) :
    BaseAppCompatDialogFragment(), View.OnClickListener {

    private var _binding: FragmentHttpInputDialogBinding? = null
    private val binding: FragmentHttpInputDialogBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHttpInputDialogBinding.inflate(inflater, container, false)
        binding.btnChange.setOnClickListener(this)
        binding.btnClear.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        val address = binding.etAddress.text.toString().trim()
        val addressUpload = binding.etAddressUpload.text.toString().trim()
        callback?.onDismiss(address, addressUpload)
        dismissAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback = null
    }

    companion object {
        @JvmStatic
        fun newInstance(callback: OnDialogDismissListener): HttpInputDialogFragment {
            return HttpInputDialogFragment(callback)
        }
    }

}