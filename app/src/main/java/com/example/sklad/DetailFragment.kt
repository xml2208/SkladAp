package com.example.sklad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sklad.databinding.FragmentDetailBinding
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DetailFragment : Fragment() {
    private var _viewBinding: FragmentDetailBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val skladRepo=SkladRepository.get()

    private val currentDocument by lazy {
        skladRepo.skladCollection.document(DetailFragmentArgs.fromBundle(requireArguments()).itemId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUi()

        viewBinding.icEdit.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToEditFragment(
                    currentDocument.id
                )
            )
        }
        viewBinding.delete.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                currentDocument.delete().await()
                findNavController().popBackStack()
            }
        }
    }

    private fun updateUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            currentDocument.listenUpdatesFlow()
                .filter { it.exists() }
                .mapLatest { Item.snapshotParser.parseSnapshot(it) }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { item ->
                    with(viewBinding) {
                        nameDetail.text = getString(R.string.name_detail, item.title)
                        quantityDetail.text = getString(R.string.quantity_detail, item.quantity)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}