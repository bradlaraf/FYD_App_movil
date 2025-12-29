package com.mobile.massiveapp.ui.view.manifiesto.info.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentManifiestoDocumentoBinding
import com.mobile.massiveapp.ui.adapters.FacturasAdapter
import com.mobile.massiveapp.ui.adapters.ManifiestoDocumentoAdapter
import com.mobile.massiveapp.ui.view.facturas.InfoFacturaActivity
import com.mobile.massiveapp.ui.view.util.SendData
import com.mobile.massiveapp.ui.viewmodel.ManifiestoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManifiestoDocumentoFragment : Fragment() {
    private var _binding: FragmentManifiestoDocumentoBinding? = null
    private val binding get() = _binding!!
    private val manifiestoViewModel: ManifiestoViewModel by activityViewModels()
    private lateinit var manifiestoDocumentoAdapter: ManifiestoDocumentoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManifiestoDocumentoBinding.inflate(inflater, container, false)

        manifiestoDocumentoAdapter = ManifiestoDocumentoAdapter(emptyList(),
            onClickListener = { documento -> },
            onLongPressListener = { view, documento ->})
        binding.rvManifiestoDocumentos.adapter = manifiestoDocumentoAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        manifiestoViewModel.dataGetAllManifiestoDocumento.observe(viewLifecycleOwner){ listaDocumentos->
            manifiestoDocumentoAdapter.updateData(listaDocumentos)
        }

        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}