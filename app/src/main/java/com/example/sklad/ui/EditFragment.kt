package com.example.sklad.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.sklad.data.SkladRepository
import com.example.sklad.model.Item
import com.example.sklad.databinding.FragmentEditBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditFragment : DialogFragment() {
    private var _viewBinding: FragmentEditBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val skladRepo by lazy { SkladRepository(context!!.applicationContext) }

    companion object {
        fun newInstance(id: String): EditFragment {
            val args = Bundle().apply {
                putString("id", id)
            }
            return EditFragment().apply {
                arguments = args
            }
        }
    }

    private val id: String by lazy { arguments!!.getString("id")!! }
    private val currentDocument by lazy {
        skladRepo.skladCollection.document(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentEditBinding.inflate(LayoutInflater.from(context), container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            val snapshot = currentDocument.get().await()
            val item = Item.snapshotParser.parseSnapshot(snapshot)
            updateUi(item.title, item.quantity)
        }

        viewBinding.saveBtn.setOnClickListener {
            updateItem()
            dismiss()
        }
    }

    private fun updateUi(title: String, quantity: Long) {
        viewBinding.apply {
            itemName.setText(title, TextView.BufferType.EDITABLE)
            iQuantity.apply {
                setText(quantity.toString(), TextView.BufferType.EDITABLE)
                requestFocus()
                setSelection(text!!.length)
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }


    private fun updateItem() = lifecycleScope.launch {
        val name = viewBinding.itemName.text.toString()
        val quantity = viewBinding.iQuantity.text.toString()

        currentDocument.apply {
            update(Item(name, quantity.toLong()).asMap()).await()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}