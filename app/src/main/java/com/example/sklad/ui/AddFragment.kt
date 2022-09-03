package com.example.sklad.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.sklad.data.SkladRepository
import com.example.sklad.model.Item
import com.example.sklad.databinding.DialogFragmentBinding

class AddFragment : DialogFragment() {
    private var _binding: DialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val skladRepo by lazy { SkladRepository.get() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        binding.saveBtn.setOnClickListener {
            addItem()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addItem() {
        val name = binding.itemName.text.toString()
        val quantity = binding.itemQuantity.text.toString().toLong()
        skladRepo.skladCollection.add(Item(name, quantity).asMap())
    }

}
