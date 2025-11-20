package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentInfoContactosBinding
import com.mobile.massiveapp.ui.adapters.SocioContactosAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.viewmodel.SocioContactoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoContactosFragment : Fragment() {
    private lateinit var socioContactosAdapter: SocioContactosAdapter
    private var _binding: FragmentInfoContactosBinding? = null
    private val binding get() = _binding!!
    private val socioContactosViewModel: SocioContactoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoContactosBinding.inflate(inflater, container, false)
        socioContactosAdapter = SocioContactosAdapter(emptyList()){ contacto->

            val lista: List<HashMap<String, Pair<Int, String>>> = listOf(
                hashMapOf("N° Documento" to Pair(R.drawable.icon_id, contacto.LicTradNum)),
                hashMapOf("Celular" to Pair(R.drawable.icon_celular, contacto.Cellolar)),
                hashMapOf("Telefono 1" to Pair(R.drawable.icon_telefono, contacto.Tel1)),
                hashMapOf("Correo Electrónico" to Pair(R.drawable.icon_mail, contacto.E_Mail)),
            )

            BaseBottomSheetCustomDialog(
                R.drawable.icon_contact_band,
                requireActivity(),
                contacto.Name,
                contacto.CardCode
            ).showBottomSheetDialog(
                lista
            )
        }
        binding.rvClienteInfoContactos.adapter = socioContactosAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        socioContactosViewModel.dataGetSocioContactosPorCardCode.observe(viewLifecycleOwner){ listaContactos->
            try {
                socioContactosAdapter.updateData(listaContactos)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(cardCode: String) =
            InfoContactosFragment().apply {
                arguments = Bundle().apply {
                    putString("cardCodeCliente", cardCode)

                }
            }
    }
}