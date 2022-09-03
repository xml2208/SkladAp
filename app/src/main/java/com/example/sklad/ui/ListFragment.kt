package com.example.sklad.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sklad.data.SkladRepository
import com.example.sklad.model.Item
import com.example.sklad.databinding.FragmentListBinding
import com.example.sklad.listenUpdatesFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val skladRepo by lazy { SkladRepository.get() }
    private val itemAdapter: ItemAdapter = ItemAdapter { id ->
        EditFragment.newInstance(id).show(childFragmentManager, "EditFragmentDialog ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            skladRepo.skladCollection.apply {
                listenUpdatesFlow()
                    .mapLatest { it ->
                        it.documents.map {
                            val item = Item.snapshotParser.parseSnapshot(it)
                            ItemAdapter.ListItem(it.id, item.title, item.quantity)
                        }
                    }
                    .mapLatest { items ->
                        items.sortedBy { it.quantity }
                    }
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect(itemAdapter::submitList)
            }
        }
        with(binding.rView) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
        }
        binding.addBtn.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        AddFragment().show(childFragmentManager, "asd")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
