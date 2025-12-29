package com.mobile.massiveapp.ui.view.manifiesto.info.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentManifiestoDocumentoBinding
import com.mobile.massiveapp.domain.model.DoFacturaView
import com.mobile.massiveapp.domain.model.DoManifiestoDocumentoView
import com.mobile.massiveapp.ui.adapters.ManifiestoDocumentoAdapter
import com.mobile.massiveapp.ui.base.BaseSimpleInformativeDialog
import com.mobile.massiveapp.ui.view.cobranzas.NuevaCobranzaActivity
import com.mobile.massiveapp.ui.view.facturas.InfoFacturaActivity
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
            onLongPressListener = { view, documento ->
                showPopupMenu(view, documento)
            })
        binding.rvManifiestoDocumentos.adapter = manifiestoDocumentoAdapter
        return binding.root
    }

    private fun showPopupMenu(view: View, documento: DoManifiestoDocumentoView) {
        val popupMenu = PopupMenu(requireActivity(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_facturas_editar_cobrar, popupMenu.menu)

        popupMenu.menu?.findItem(R.id.nav_item_info_factura)?.isVisible = false

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.nav_item_cobrar_factura ->{
                    if (documento.Saldo <= 0.0){
                        BaseSimpleInformativeDialog(
                            titulo = "Factura Pagada",
                            mensaje = "Esta factura ya no tiene deuda"
                        ).show(childFragmentManager, "mensaje")
                    } else {
                        Intent(requireActivity(), NuevaCobranzaActivity::class.java)
                            .putExtra("docEntry", documento.DocEntry)
                            .putExtra("cardCode", documento.CodigoSocio)
                            .also { startActivity(it) }
                    }
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
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