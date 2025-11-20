package com.mobile.massiveapp.ui.view.pedidocliente.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.databinding.FragmentPedidoActualizarDetalleNuevoBinding
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import com.mobile.massiveapp.ui.base.BaseDialogChecklistWithId
import com.mobile.massiveapp.ui.base.BaseDialogEdtCantidadPedido
import com.mobile.massiveapp.ui.view.pedidocliente.BuscarArticuloActivity
import com.mobile.massiveapp.ui.view.util.actualizarDetalleDePedido
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.PedidoViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PedidoActualizarDetalleNuevoFragment : Fragment() {
    private var _binding: FragmentPedidoActualizarDetalleNuevoBinding? = null
    private val binding get() = _binding!!
    private var hashInfo = HashMap<String, Any>()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by activityViewModels()
    private val articuloViewModel: ArticuloViewModel by activityViewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()
    private val pedidoViewModel: PedidoViewModel by activityViewModels()
    private var itemCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoActualizarDetalleNuevoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemCode = providerViewModel.data.toString()

        //Click SELECCIONAR ARTICULO
        binding.clNPArtInfoArticulo.setOnClickListener {
            Intent(requireContext(), BuscarArticuloActivity::class.java).also {
                startForArticuloSeleccionadoResult.launch(it)
            }
        }


        //Get DATOS DEL USUARIO
        usuarioViewModel.dataGetUsuarioFromDatabase.observe(viewLifecycleOwner){ usuario->
            try {
                hashInfo["codigoUsuario"] = usuario.Code
                hashInfo["codigoAlmacen"] = usuario.DefaultWarehouse
                hashInfo["codigoImpuesto"] = usuario.DefaultTaxCode
                hashInfo["CanEditPrice"] = usuario.CanEditPrice
                hashInfo["listaPrecioCodigo"] = usuario.DefaultPriceList
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //Set IMPUESTO - IMPUESTO EXONERADO
        generalViewModel.dataGetImpuestoDefault.observe(viewLifecycleOwner){ impuestoDefault->
            try {
                binding.txvNpArtInfoImpuestoValue.text = impuestoDefault.Name
            } catch (e: Exception){
                e.printStackTrace()
            }
        }


        //Set ALMACENES
        articuloViewModel.dataGetAllArticuloAlmacenes.observe(viewLifecycleOwner) { almacenes ->
            try {
                binding.txvNpArtInfoAlmacenValue.text = almacenes[0].WhsName
                hashInfo["codigoAlmacen"] = almacenes[0].WhsCode
            } catch (e: Exception){
                e.printStackTrace()
            }

            binding.clNpArtInfoAlmacen.setOnClickListener {
                BaseDialogChecklistWithId(
                    binding.txvNpArtInfoAlmacenValue.text.toString(),
                    almacenes.map { it.WhsName }
                ){ almacenSeleccionado, id ->
                    if (almacenSeleccionado.isNotEmpty()){
                        binding.txvNpArtInfoAlmacenValue.text = almacenSeleccionado
                        hashInfo["codigoAlmacen"] = almacenes[id].WhsCode
                        articuloViewModel.getArticuloCantidadesPorItemCodeYWhsCode(
                            itemCode = itemCode,
                            whsCode = almacenes[id].WhsCode
                        )
                    }
                }.show(parentFragmentManager, "almacenes")
            }
        }


        //Set LISTA DE PRECIOS
        articuloViewModel.dataGetAllArticuloListaPrecios.observe(viewLifecycleOwner){ listasPrecio->
            try {
                binding.txvNpArtInfoListaPreciosValue.text = listasPrecio[0].ListName
                hashInfo["listaPrecioCodigo"] = listasPrecio[0].ListNum
            } catch (e: Exception){
                e.printStackTrace()
            }
            binding.clNpArtInfoListaPrecios.setOnClickListener {
                try {
                    if (hashInfo["CanEditPrice"] as String == "N"){
                        throw Exception("No tiene permisos para editar precios")
                    }
                    BaseDialogChecklistWithId(
                        binding.txvNpArtInfoListaPreciosValue.text.toString(),
                        listasPrecio.map { it.ListName }
                    ){ listaSeleccionada, id->
                        if (listaSeleccionada.isNotEmpty()){
                            binding.txvNpArtInfoListaPreciosValue.text = listaSeleccionada
                            hashInfo["listaPrecioCodigo"] = listasPrecio[id].ListNum

                            pedidoViewModel.getUnidadMedidaYEquivalencia(
                                itemCode,
                                binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                                hashInfo["listaPrecioCodigo"] as Int
                            )
                        }
                    }.show(parentFragmentManager, "listaPrecios")
                } catch (e: Exception){
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }



        //Set UNIDAD DE MEDIDA
        binding.clNpArtInfoUnidadMedida.setOnClickListener {
            pedidoViewModel.getAllUnidadesDeMedidaPorGrupoUnidadDeMedida(binding.txvNPArtInfoArticuloValue.text.toString())
        }
        pedidoViewModel.dataGetAllUnidadesDeMedidaPorItemCode.observe(viewLifecycleOwner){ unidadesDeMedida->
            BaseDialogChecklistWithId(
                binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                unidadesDeMedida.map { it.UomName }
            ){ unidadMedidaSeleccionada, id->
                if (unidadMedidaSeleccionada.isNotEmpty()){
                    binding.txvNpArtInfoUnidadMedidaValue.text = unidadMedidaSeleccionada

                    pedidoViewModel.getUnidadMedidaYEquivalencia(
                        binding.txvNPArtInfoArticuloValue.text.toString(),
                        unidadMedidaSeleccionada,
                        hashInfo["listaPrecioCodigo"] as Int
                    )
                }
            }.show(parentFragmentManager, "unidadMedida")
        }




        //Set CANTIDAD
        binding.txvNpArtInfoCantidadValue.text = "0"
        binding.clNpArtInfoCantidad.setOnClickListener {
            try {
                if (binding.txvNpArtInfoGrupoUnidadMedidaValue.text.isEmpty()){
                    throw Exception("Debe seleccionar un articulo")
                }

                articuloViewModel.getArticuloCantidadPedido(
                    binding.txvNPArtInfoArticuloValue.text.toString(),
                    binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                    hashInfo["codigoAlmacen"] as String
                )
            } catch (e:Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
        articuloViewModel.dataGetArticuloCantidadPedido.observe(viewLifecycleOwner){
            BaseDialogEdtCantidadPedido(
                binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                it.toDouble(),
                binding.txvNpArtInfoCantidadValue.text.toString()
            ){ cantidad->
                if (cantidad.isNotEmpty()){
                    binding.txvNpArtInfoCantidadValue.text = cantidad
                    val precio = binding.txvNpArtInfoPrecioUnitarioValue.text.toString().toDouble()
                    setTotalValue(precio)
                }
            }.show(parentFragmentManager, "cantidad")
        }




        //Live data del Precio del Pedido
        pedidoViewModel.dataGetUnidadMedidaYEquivalencia.observe(viewLifecycleOwner){
            try {
                hashInfo["uomEntry"] = it.UomEntry
                setTotalValue(it.PrecioFinal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        //LiveData PRECIO UNITARIO por Lista de Precios
        articuloViewModel.dataGetArticuloPrecioPorItemCodeYPriceList.observe(viewLifecycleOwner){ precio->
            try {
                binding.txvNpArtInfoPrecioUnitarioValue.text = precio.Price.toString()
                binding.txvNpArtInfoPrecioBrutoValue.text = precio.Price.toString()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        //LiveData del ARTICULO INFO
        articuloViewModel.dataGetArticuloPedidoInfo.observe(viewLifecycleOwner){ articuloSeleccionado->
            try {
                binding.txvNPArtInfoArticuloValue.text = articuloSeleccionado.ItemCode                  //codigo
                binding.txvNpArtInfoDescripcionValue.text = articuloSeleccionado.ItemName               //descripcion
                binding.txvNpArtInfoUnidadMedidaValue.text = articuloSeleccionado.SalUnitMsr            //unidad de medida
                binding.txvNpArtInfoCantidadValue.text = "0"                                            //cantidad

                binding.txvNpArtInfoGrupoUnidadMedidaValue.text = articuloSeleccionado.UgpName          //grupo de unidad de medida
                binding.txvNpArtInfoUnidadMedidaValue.text = articuloSeleccionado.UomName               //unidad de medida
                binding.txvNpArtInfoPorcentajeDescuentoValue.text = "0.00"                              //porcentaje de descuento
                binding.txvNpArtInfoTotalValue.text = articuloSeleccionado.price.toString()
                //total

                hashInfo["uomEntry"] = articuloSeleccionado.UomEntry


                pedidoViewModel.getUnidadMedidaYEquivalencia(
                    articuloSeleccionado.ItemCode,
                    binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
                    hashInfo["listaPrecioCodigo"] as Int
                )
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }



    fun llenarDatosDelArticulo(articuloSeleccionado: DoArticuloPedidoInfo) {
        binding.txvNPArtInfoArticuloValue.text = articuloSeleccionado.ItemCode                  //codigo
        binding.txvNpArtInfoDescripcionValue.text = articuloSeleccionado.ItemName               //descripcion


        binding.txvNpArtInfoUnidadMedidaValue.text = articuloSeleccionado.SalUnitMsr            //unidad de medida
        if (articuloSeleccionado.UgpName == "MANUAL"){
            binding.clNpArtInfoUnidadMedida.isClickable = false
            binding.imvUnidadMedida.isVisible = false
        } else {
            binding.clNpArtInfoUnidadMedida.isClickable = true
            binding.imvUnidadMedida.isVisible = true
        }


        binding.txvNpArtInfoCantidadValue.text =  "0"                                           //cantidad

        binding.txvNpArtInfoGrupoUnidadMedidaValue.text = articuloSeleccionado.UgpName          //grupo de unidad de medida
        binding.txvNpArtInfoUnidadMedidaValue.text = articuloSeleccionado.UomName               //unidad de medida
        binding.txvNpArtInfoPorcentajeDescuentoValue.text = "0.00"                              //descuento
    }


    fun setTotalValue(precioUnitario: Double){
        val newPrecio = precioUnitario.format(2)
        val cantidad = binding.txvNpArtInfoCantidadValue.text.toString().toDouble().format(2)
        val total = (cantidad * newPrecio).format(2)
        val newTotal = total.format(2)
        binding.txvNpArtInfoTotalValue.text = newTotal.toString()
        binding.txvNpArtInfoPrecioUnitarioValue.text = newPrecio.toString()    //precio unitario
        binding.txvNpArtInfoPrecioBrutoValue.text = newPrecio.toString()       //precio bruto
    }




    fun savePedidoDetalle(accDocEntry: String, lineNum: Int): ClientePedidoDetalle =
        actualizarDetalleDePedido(
            usuario =               hashInfo["codigoUsuario"] as String,
            accDocEntry =           accDocEntry,
            codigo =                binding.txvNPArtInfoArticuloValue.text.toString(),
            nombre =                binding.txvNpArtInfoDescripcionValue.text.toString().replace("\n", " "),
            unidadMedida =          binding.txvNpArtInfoUnidadMedidaValue.text.toString(),
            cantidad =              binding.txvNpArtInfoCantidadValue.text.toString().toDouble(),
            grupoUM =               binding.txvNpArtInfoGrupoUnidadMedidaValue.text.toString(),
            precio =                binding.txvNpArtInfoPrecioUnitarioValue.text.toString().toDouble(),
            precioBruto =           binding.txvNpArtInfoPrecioBrutoValue.text.toString().toDouble(),
            porcentajeDescuento =   binding.txvNpArtInfoPorcentajeDescuentoValue.text.toString().toDouble(),
            total =                 binding.txvNpArtInfoTotalValue.text.toString().toDouble(),
            lineNum =               lineNum,
            fechaActual =           getFechaActual(),
            horaActual =            getHoraActual(),
            impuesto =              0.0,
            codigoImpuesto =        hashInfo["codigoImpuesto"] as String,
            listaPrecios =          hashInfo["listaPrecioCodigo"] as Int,
            codigoAlmacen =         hashInfo["codigoAlmacen"] as String,
            fechaActualizacion =    getFechaActual(),
            horaActualizacion =     getHoraActual(),
            migrado =               "N",
            accAction =             "I",
            uomEntry =              hashInfo["uomEntry"] as Int,
            docEntry =              -1
        )

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PedidoActualizarDetalleNuevoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

        //Resultado del Articulo seleccionado
    val startForArticuloSeleccionadoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode.equals(AppCompatActivity.RESULT_OK)){
            val data = result.data
            val itemCodeArticuloSeleccionado = data?.getStringExtra("itemCodeArticulo")
            if (!itemCodeArticuloSeleccionado.isNullOrEmpty()){
                providerViewModel.saveItemCode(itemCodeArticuloSeleccionado)
                providerViewModel.saveWhsCode(hashInfo["codigoAlmacen"] as String)
            }
        }
    }
}