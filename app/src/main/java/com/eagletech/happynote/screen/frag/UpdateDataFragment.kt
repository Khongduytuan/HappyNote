package com.eagletech.happynote.screen.frag

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.eagletech.happynote.R
import com.eagletech.happynote.databinding.FragmentDataBinding
import com.eagletech.happynote.databinding.FragmentUpdateDataBinding
import com.eagletech.happynote.entities.Data
import com.eagletech.happynote.viewml.DataViewModel


class UpdateDataFragment : Fragment() {

    private var _updateDataBinding: FragmentUpdateDataBinding? = null
    private val updateDataBinding get() = _updateDataBinding
    private lateinit var dataUpdate: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _updateDataBinding = FragmentUpdateDataBinding.inflate(inflater, container, false)
        return updateDataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val data = bundle?.getSerializable("dataUpdate") as? Data
        Log.d("data", data?.dataTitle + " " + data?.dataContent)
        if (data != null) {
            dataUpdate = data
            updateDataBinding?.edtTitleDataUpdate?.setText(dataUpdate.dataTitle)
            updateDataBinding?.etdBodyDataUpdate?.setText(dataUpdate.dataContent)
        }
        updateDataBinding?.doneDataUpdate?.setOnClickListener {
            val titleData = updateDataBinding?.edtTitleDataUpdate?.text.toString().trim()
            val contentData = updateDataBinding?.etdBodyDataUpdate?.text.toString().trim()
            if (titleData.isNotEmpty()) {
                val data = Data(dataUpdate.idData, titleData, contentData)
                dataViewModel.updateData(data)
                view.findNavController().navigate(R.id.action_updateDataFragment_to_dataFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter data title",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        updateDataBinding?.deleteDataUpdate?.setOnClickListener {
            deleteData()
        }

    }

    private fun deleteData() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Data")
            setMessage("Are you sure you want to delete this data?")
            setPositiveButton("OK") { _, _ ->
                dataViewModel.deleteData(dataUpdate)
                view?.findNavController()?.navigate(
                    R.id.action_updateDataFragment_to_dataFragment
                )
            }
            setNegativeButton("NO", null)
        }.create().show()

    }



    private val dataViewModel: DataViewModel by lazy {
        val application = requireActivity().application
        ViewModelProvider(
            this,
            DataViewModel.DataViewModelFactory(application)
        )[DataViewModel::class.java]
    }


}