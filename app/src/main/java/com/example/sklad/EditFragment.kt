package com.example.sklad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sklad.databinding.FragmentEditBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditFragment : Fragment() {

    private var _viewBinding: FragmentEditBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val skladRepo by lazy { SkladRepository(context!!.applicationContext) }

    private val currentDocument by lazy {
        skladRepo.skladCollection.document(DetailFragmentArgs.fromBundle(requireArguments()).itemId)
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
        }
        viewBinding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateUi(title: String, quantity: Long) {
        viewBinding.apply {
            itemName.setText(title, TextView.BufferType.EDITABLE)
            iQuantity.setText(quantity.toString(), TextView.BufferType.EDITABLE)
        }
    }

    private fun updateItem() = lifecycleScope.launch {
        val name = viewBinding.itemName.text.toString()
        val quantity = viewBinding.iQuantity.text.toString().toLong()

        currentDocument.update(Item(name, quantity).asMap()).await()

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}