package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentClienteInfoCondicionesBinding
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel

class ClienteInfoCondicionesFragment : Fragment() {
    private var _binding: FragmentClienteInfoCondicionesBinding? = null
    private val binding get() = _binding!!
    private val socioViewModel: SocioViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClienteInfoCondicionesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        socioViewModel.dataGetInfoClientePorCardCode.observe(viewLifecycleOwner){ clienteInfo->
            try {
                binding.txvClienteInfoListaPrecioValue.text = clienteInfo.ListName
                binding.txvClienteInfoCondicionPagoValue.text = clienteInfo.PymntGroup
                binding.txvClienteInfoIndicadorValue.text = clienteInfo.Indicador
                binding.txvClienteInfoZonaValue.text = clienteInfo.Zona

            } catch (e: Exception){
                e.printStackTrace()
            }

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClienteInfoCondicionesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}