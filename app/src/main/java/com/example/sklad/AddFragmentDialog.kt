package com.example.sklad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.sklad.databinding.DialogFragmentBinding

class AddFragmentDialog : DialogFragment() {

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
//        if (name.isEmpty() && quantity.toString().isEmpty()) {
//        }
        skladRepo.skladCollection.add(Item(name, quantity).asMap())
    }

}
