package com.eagletech.happynote.screen.frag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.eagletech.happynote.R
import com.eagletech.happynote.databinding.FragmentInsertDataBinding
import com.eagletech.happynote.datauser.DataSharedPreferences
import com.eagletech.happynote.entities.Data
import com.eagletech.happynote.screen.acts.PaymentActivity
import com.eagletech.happynote.viewml.DataViewModel


class InsertDataFragment : Fragment(R.layout.fragment_insert_data) {
    private var _insertDataBinding: FragmentInsertDataBinding? = null
    private val insertDataBinding get() = _insertDataBinding!!
    private lateinit var dataSharedPreferences: DataSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSharedPreferences = DataSharedPreferences.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _insertDataBinding = FragmentInsertDataBinding.inflate(inflater, container, false)
        insertDataBinding.doneData.setOnClickListener {
            if (dataSharedPreferences.getLives() > 0 || dataSharedPreferences.isPremium == true) {
                saveData(requireView())
            } else {
                Toast.makeText(
                    requireContext(),
                    "You must purchase additional usage!!!",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                requireActivity().finish()
            }
        }
        return insertDataBinding.root
    }


    private fun saveData(view: View) {
        val tvDataTitle = insertDataBinding.edtTitleData.text.toString().trim()
        val tvDataBody = insertDataBinding.etdBodyData.text.toString().trim()

        if (tvDataTitle.isNotEmpty()) {
            val data = Data(0, tvDataTitle, tvDataBody)
            dataViewModel.insertData(data)
            dataSharedPreferences.removeLife()
            Log.d("data insert", dataSharedPreferences.getLives().toString())
            Toast.makeText(
                requireContext(),
                "Save Data Successfully!!!",
                Toast.LENGTH_LONG
            ).show()
            view.findNavController().navigate(R.id.action_insertDataFragment_to_dataFragment)

        } else {
            Toast.makeText(
                requireContext(),
                "Enter your title",
                Toast.LENGTH_LONG
            ).show()
        }
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
        _insertDataBinding = null
    }

}