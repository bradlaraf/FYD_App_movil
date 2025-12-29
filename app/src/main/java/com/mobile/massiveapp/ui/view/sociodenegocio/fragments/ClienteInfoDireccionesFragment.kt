package com.mobile.massiveapp.ui.view.sociodenegocio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentClienteInfoDireccionesBinding
import com.mobile.massiveapp.ui.adapters.SocioDireccionViewAdapter
import com.mobile.massiveapp.ui.adapters.SocioDireccionesAdapter
import com.mobile.massiveapp.ui.base.BaseBottomSheetCustomDialog
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.SocioDireccionesViewModel


class ClienteInfoDireccionesFragment : Fragment() {
    private var _binding: FragmentClienteInfoDireccionesBinding? = null
    private lateinit var socioDireccionesAdapter: SocioDireccionViewAdapter
    private val binding get() = _binding!!
    private val direccionesViewModel: SocioDireccionesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClienteInfoDireccionesBinding.inflate(inflater, container, false)
            //Inicializacion del adapter

        socioDireccionesAdapter = SocioDireccionViewAdapter(emptyList()){ direccion->

            val lista: List<HashMap<String, Pair<Int, String>>> = listOf(
                hashMapOf("País" to Pair(R.drawable.icon_pais, direccion.Pais)),
                hashMapOf("Departamento" to Pair(R.drawable.icon_departamento, direccion.Departamento)),
                hashMapOf("Provincia" to Pair(R.drawable.icon_provincia, direccion.Provincia)),
                hashMapOf("Distrito" to Pair(R.drawable.icon_distrito, direccion.Distrito)),
                hashMapOf("Calle" to Pair(R.drawable.icon_calle, direccion.Calle)),
                hashMapOf("Referencia" to Pair(R.drawable.icon_comentario, direccion.Referencia)),
                hashMapOf("Geolocalización" to Pair(R.drawable.icon_ubicacion, "${direccion.Longitud}-${direccion.Latitud}" )),
                hashMapOf("Vendedor" to Pair(R.drawable.icon_comentario, direccion.Vendedor)),
                hashMapOf("Zona" to Pair(R.drawable.icon_check, direccion.Zona)),
            )

            BaseBottomSheetCustomDialog(
                R.drawable.icon_direccion,
                requireActivity(),
                direccion.Direccion,
                direccion.CardCode
            ).showBottomSheetDialog(
                lista
            ){}
        }
        binding.rvClienteInfoDirecciones.adapter = socioDireccionesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        direccionesViewModel.dataGetAllDireccionesView.observe(viewLifecycleOwner) { listaDirecciones->
            try {
                socioDireccionesAdapter.updateList(listaDirecciones)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }




    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClienteInfoDireccionesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}