package com.example.sklad.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sklad.data.SkladRepository
import com.example.sklad.model.Item
import com.example.sklad.databinding.JustListBinding
import com.example.sklad.listenUpdatesFlow
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class JustList : Fragment() {
    private var _binding: JustListBinding? = null
    private val binding get() = _binding!!
    private val skladRepo by lazy { SkladRepository.get() }

    private val itemAdapter: ItemAdapter = ItemAdapter { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JustListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            skladRepo.skladCollection.apply {
                orderBy("quantity", Query.Direction.ASCENDING)
                orderBy("title", Query.Direction.ASCENDING)
                listenUpdatesFlow()
                    .mapLatest { it ->
                        it.documents.map {
                            val item = Item.snapshotParser.parseSnapshot(it)
                            ItemAdapter.ListItem(it.id, item.title, item.quantity)
                        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}