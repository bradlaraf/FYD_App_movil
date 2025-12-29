package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.databinding.FragmentInfoGeneralBinding
import com.mobile.massiveapp.ui.viewmodel.SocioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoGeneralFragment : Fragment() {
    private var _binding: FragmentInfoGeneralBinding? = null
    private val binding get() = _binding!!
    private val socioViewModel: SocioViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socioViewModel.dataGetInfoClientePorCardCode.observe(viewLifecycleOwner){ infoCliente->
            try {
                binding.txvClienteInfoTipoPersonaValue.text = infoCliente.U_MSV_LO_TIPOPER
                binding.txvClienteInfoTipoDocumentoValue.text = infoCliente.U_MSV_LO_TIPODOC
                binding.txvClienteInfoNumeroDocumentoValue.text = infoCliente.LicTradNum
                binding.txvClienteInfoGrupoValue.text = infoCliente.GroupName
                binding.txvClienteInfoMonedaValue.text = infoCliente.Currency
                binding.txvClienteInfoCorreoElectronicoValue.text = infoCliente.E_Mail

                binding.txvClienteInfoTelefono1Value.text = infoCliente.Phone1
                binding.txvClienteInfoTelefono2Value.text = infoCliente.Phone2
                binding.txvClienteInfoTelefonoMovilValue.text = infoCliente.Cellular
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion   object {
        @JvmStatic
        fun newInstance() = InfoGeneralFragment(
            ).apply {

        }

    }
}