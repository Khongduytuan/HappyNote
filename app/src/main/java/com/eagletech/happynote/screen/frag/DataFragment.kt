package com.eagletech.happynote.screen.frag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eagletech.happynote.R
import com.eagletech.happynote.adap.DataAdapter
import com.eagletech.happynote.databinding.FragmentDataBinding
import com.eagletech.happynote.entities.Data
import com.eagletech.happynote.statedata.DataState
import com.eagletech.happynote.viewml.DataViewModel
import kotlinx.coroutines.launch


class DataFragment : Fragment(R.layout.fragment_data) {

    private var _dataFragBinding: FragmentDataBinding? = null
    private val dataFragBinding get() = _dataFragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dataFragBinding = FragmentDataBinding.inflate(inflater, container, false)
        return dataFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataFragBinding.insertData.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_dataFragment_to_insertDataFragment)
        }
        initControls()
    }

    private fun initControls() {
        val adapter: DataAdapter = DataAdapter(requireContext(), onClickItem)
        dataFragBinding.recyclerData.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
        dataFragBinding.recyclerData.adapter = adapter
        dataViewModel.getAllData()

        lifecycleScope.launch {
            dataViewModel._dataSateFlow.collect { it ->
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Failure -> {

                    }

                    is DataState.Success -> {
                        adapter.setData(it.data)
                        dataFragBinding.tvNoData.isVisible = false
                        dataFragBinding.recyclerData.isVisible = true
                    }

                    is DataState.Empty -> {
                        adapter.setData(it.data)
                        dataFragBinding.tvNoData.isVisible = true
                        dataFragBinding.recyclerData.isVisible = false

                    }

                }

            }
        }

    }

    private val onClickItem: (Data) -> Unit = {
        val bundle = bundleOf(
            "dataUpdate" to it
        )
        findNavController().navigate(R.id.action_dataFragment_to_updateDataFragment, bundle)
    }


    private val dataViewModel: DataViewModel by lazy {
        val application = requireActivity().application
        ViewModelProvider(
            this,
            DataViewModel.DataViewModelFactory(application)
        )[DataViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _dataFragBinding = null
    }

}