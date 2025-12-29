package com.mobile.massiveapp.ui.view.usuarios.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.FragmentUsuarioGeneralBinding
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.ui.base.BaseDialogCheckListWithViewAndId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.view.util.getBoolByYorN
import com.mobile.massiveapp.ui.view.util.getBoolFromAccLocked
import com.mobile.massiveapp.ui.view.util.getStringBool
import com.mobile.massiveapp.ui.view.util.getStringForAccLocked
import com.mobile.massiveapp.ui.view.util.observeOnce
import com.mobile.massiveapp.ui.view.util.showMessage
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuarioGeneralFragment : Fragment() {
    private var _binding: FragmentUsuarioGeneralBinding? = null
    private val binding get() = _binding!!
    private val usuariosViewModel: UsuariosViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val articulosViewModel: ArticuloViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by activityViewModels()

    private var infoUsuario = HashMap<String, Any>()
    private val OBJ_CODE_CLIENTES = 2
    private val OBJ_CODE_PEDIDOS = 17
    private val OBJ_CODE_PAGOS = 24

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioGeneralBinding.inflate(inflater, container, false)
        /*setDefaultUi()*/
        setValoresIniciales()
        val onlyShowInfo = requireActivity().intent.getBooleanExtra("showInfo", false)
        if (onlyShowInfo){
            hideImages()
            setNoSelectableContainers()
        }
        val usuarioCode = requireActivity().intent.getStringExtra("usuarioCode").toString()
        usuariosViewModel.getUsuarioInfo(usuarioCode)
        binding.clUsuarioGeneralPassword.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Mostrar texto sin ocultar (sin modo contraseña)
                    binding.txvUsuarioGeneralPasswordValue.inputType = InputType.TYPE_CLASS_TEXT
                    binding.imvPassword.setImageResource(R.drawable.icon_eye_open)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Volver a ocultar el texto (modo contraseña)
                    binding.txvUsuarioGeneralPasswordValue.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.imvPassword.setImageResource(R.drawable.icon_eye_closed)
                }
            }
            true
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        usuariosViewModel.dataGetusuarioInfo.observe(viewLifecycleOwner){ infoUsuario->
            binding.txvUsuarioGeneralCodigoValue.text = infoUsuario.Codigo
            binding.txvUsuarioGeneralPasswordValue.text = infoUsuario.Password
            binding.txvUsuarioGeneralIDTelefonoValue.text = infoUsuario.IdTelefono
            binding.txvUsuarioGeneralNombreValue.text = infoUsuario.Nombre
            binding.txvUsuarioGeneralCorreoValue.text = infoUsuario.Correo
            binding.txvUsuarioGeneralTelefonoValue.text = infoUsuario.Telefono
            binding.txvUsuarioGeneralUsuarioZonaValue.text = infoUsuario.Zona
            binding.txvUsuarioGeneralVendedorValue.text = infoUsuario.Vendedor
            binding.txvUsuarioGeneralSerieClientesValue.text = ""
            binding.txvUsuarioGeneralSeriePedidosValue.text = ""
            binding.txvUsuarioGeneralSeriePagosValue.text = ""
            binding.txvUsuarioGeneralListaPrecioValue.text = infoUsuario.ListaPrecio
            binding.txvUsuarioGeneralImpuestoValue.text = infoUsuario.Impuesto
            binding.txvUsuarioGeneralMonedaValue.text = infoUsuario.Moneda
            binding.txvUsuarioGeneralProyectoValue.text = ""
            binding.txvUsuarioGeneralAlmacenValue.text = infoUsuario.Almacen
            binding.txvUsuarioGeneralCuentaEfectivoValue.text = ""
            binding.txvUsuarioGeneralCuentaTransferenciaValue.text = ""
            binding.txvUsuarioGeneralCuentaDepositoValue.text = ""
            binding.txvUsuarioGeneralCuentaChequeValue.text = ""
            binding.txvUsuarioGeneralConductorValue.text = ""

            binding.cbUsuarioActivo.isChecked = infoUsuario.Activo.getBoolFromAccLocked()
            binding.cbUsuarioSuperUsuario.isChecked = infoUsuario.SuperUser.getBoolByYorN()
            binding.cbUsuarioEdicionPrecios.isChecked = infoUsuario.EditarListaPrecios.getBoolByYorN()
            binding.cbUsuarioPuedeCrear.isChecked = infoUsuario.PuedeCrear.getBoolByYorN()
            binding.cbUsuarioPuedeActualizar.isChecked = infoUsuario.PuedeActualizar.getBoolByYorN()
            binding.cbUsuarioPuedeDeclinar.isChecked = infoUsuario.PuedeDeclinar.getBoolByYorN()
            binding.cbUsuarioPuedeAprovar.isChecked = infoUsuario.PuedeAprobar.getBoolByYorN()
            binding.cbUsuarioSesionActiva.isChecked = infoUsuario.SesionActiva.getBoolByYorN()
        }

        providerViewModel.dataRecolectarData.observeOnce(viewLifecycleOwner){
            val fieldEmpty = validateEmptyFields()
            if(fieldEmpty.isEmpty()){
                providerViewModel.saveContador(1)
                providerViewModel.saveInfoUsuario(getUserInfo())
            } else {
                Toast.makeText(requireContext(), "Debe completar el campo $fieldEmpty", Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setValoresIniciales() {
        infoUsuario["slpCode"] = ""
        infoUsuario["pedidoSeries"] = ""
        infoUsuario["socioSeries"] = ""
        infoUsuario["pagosSeries"] = ""
        infoUsuario["priceList"] = ""
        infoUsuario["whsCode"] = ""
        infoUsuario["currCode"] = ""
        infoUsuario["taxCode"] = ""
        infoUsuario["acctEfectivo"] = ""
        infoUsuario["acctTransferencia"] = ""
    }

    private fun setDefaultUi() {
        binding.clUsuarioGeneralActivo.setOnClickListener {
            binding.cbUsuarioActivo.isChecked = !binding.cbUsuarioActivo.isChecked
        }
        binding.clUsuarioGeneralSuperUsuario.setOnClickListener {
            binding.cbUsuarioSuperUsuario.isChecked = !binding.cbUsuarioSuperUsuario.isChecked
        }
        binding.clUsuarioGeneralEdicionPrecios.setOnClickListener {
            binding.cbUsuarioEdicionPrecios.isChecked = !binding.cbUsuarioEdicionPrecios.isChecked
        }
        binding.clUsuarioGeneralPuedeCrear.setOnClickListener {
            binding.cbUsuarioPuedeCrear.isChecked = !binding.cbUsuarioPuedeCrear.isChecked
        }
        binding.clUsuarioGeneralPuedeActualizar.setOnClickListener {
            binding.cbUsuarioPuedeActualizar.isChecked = !binding.cbUsuarioPuedeActualizar.isChecked
        }
        binding.clUsuarioGeneralPuedeDeclinar.setOnClickListener {
            binding.cbUsuarioPuedeDeclinar.isChecked = !binding.cbUsuarioPuedeDeclinar.isChecked
        }
        binding.clUsuarioGeneralPuedeAprovar.setOnClickListener {
            binding.cbUsuarioPuedeAprovar.isChecked = !binding.cbUsuarioPuedeAprovar.isChecked
        }


        //Codigo
        binding.clUsuarioGeneralCodigo.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralCodigoValue.text.toString(),
                "Ingrese el Código"
            ){ newText->
                binding.txvUsuarioGeneralCodigoValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Nombre
        binding.clUsuarioGeneralPassword.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralPasswordValue.text.toString(),
                "Ingrese la contraseña"
            ){ newText->
                binding.txvUsuarioGeneralPasswordValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Id Movil
        binding.clUsuarioGeneralIDTelefono.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralIDTelefonoValue.text.toString(),
                "Ingrese el ID del Teléfono"
            ){ newText->
                binding.txvUsuarioGeneralIDTelefonoValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Nombre
        binding.clUsuarioGeneralNombre.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralNombreValue.text.toString(),
                "Ingrese el Nombre"
            ){ newText->
                binding.txvUsuarioGeneralNombreValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Correo
        binding.clUsuarioGeneralCorreo.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralCorreoValue.text.toString(),
                "Ingrese el Correo"
            ){ newText->
                binding.txvUsuarioGeneralCorreoValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Telefono
        binding.clUsuarioGeneralTelefono.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralTelefonoValue.text.toString(),
                "Ingrese el Teléfono"
            ){ newText->
                binding.txvUsuarioGeneralTelefonoValue.text = newText
            }.show(requireActivity().supportFragmentManager, "showDialog")
        }

        //Vendedor
        binding.clUsuarioGeneralVendedor.setOnClickListener {
            generalViewModel.getAllGeneralVendedores()
            generalViewModel.dataGetAllGeneralVendedores.observeOnce(viewLifecycleOwner){ listaVendedores->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralVendedorValue.text.toString(),
                    listaVendedores.map { it.SlpName },
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralVendedorValue.text = opcionElegida
                    infoUsuario["slpCode"] = listaVendedores[id].SlpCode.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Series - Clientes
        binding.clUsuarioGeneralSerieClientes.setOnClickListener {
            generalViewModel.getAllSeriesN()
            generalViewModel.dataGetAllSeriesN.observeOnce(viewLifecycleOwner){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_CLIENTES }
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralSerieClientesValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSerieClientesValue.text = opcionElegida
                    infoUsuario["pedidoSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Series - Pedidos
        binding.clUsuarioGeneralSeriePedidos.setOnClickListener {
            generalViewModel.getAllSeriesN()
            generalViewModel.dataGetAllSeriesN.observeOnce(viewLifecycleOwner){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PEDIDOS }
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralSeriePedidosValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSeriePedidosValue.text = opcionElegida
                    infoUsuario["socioSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Series - Pagos
        binding.clUsuarioGeneralSeriePagos.setOnClickListener {
            generalViewModel.getAllSeriesN()
            generalViewModel.dataGetAllSeriesN.observeOnce(viewLifecycleOwner){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PAGOS }
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralSeriePagosValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSeriePagosValue.text = opcionElegida
                    infoUsuario["pagosSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Lista Precios
        binding.clUsuarioGeneralListaPrecio.setOnClickListener {
            articulosViewModel.getAllArticuloListaPrecios()
            articulosViewModel.dataGetAllArticuloListaPrecios.observeOnce(viewLifecycleOwner){ listaPrecios->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralListaPrecioValue.text.toString(),
                    listaPrecios.map { it.ListName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralListaPrecioValue.text = opcionElegida
                    infoUsuario["priceList"] = listaPrecios[id].ListNum.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Impuesto
        binding.clUsuarioGeneralImpuesto.setOnClickListener {
            generalViewModel.getAllGeneralImpuestos()
            generalViewModel.dataGetAllGeneralImpuestos.observeOnce(viewLifecycleOwner){ listaImpuestos->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralImpuestoValue.text.toString(),
                    listaImpuestos.map { it.Name }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralImpuestoValue.text = opcionElegida
                    infoUsuario["taxCode"] = listaImpuestos[id].Code
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Monedas
        binding.clUsuarioGeneralMoneda.setOnClickListener {
            generalViewModel.getAllGeneralMonedas()
            generalViewModel.dataGetAllGeneralMonedas.observeOnce(viewLifecycleOwner){ listaMonedas->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralMonedaValue.text.toString(),
                    listaMonedas.map { it.CurrCode }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralMonedaValue.text = opcionElegida
                    infoUsuario["currCode"] = listaMonedas[id].CurrCode.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Proyectos
        binding.clUsuarioGeneralProyecto.setOnClickListener {
        }

        //Almacenes
        binding.clUsuarioGeneralAlmacen.setOnClickListener {
            articulosViewModel.getAllArticuloAlmacenes()
            articulosViewModel.dataGetAllArticuloAlmacenes.observeOnce(viewLifecycleOwner){ listaAlmacenes->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralAlmacenValue.text.toString(),
                    listaAlmacenes.map { it.WhsName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralAlmacenValue.text = opcionElegida
                    infoUsuario["whsCode"] = listaAlmacenes[id].WhsCode
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Cuenta Efectivo
        binding.clUsuarioGeneralCuentaEfectivo.setOnClickListener {
            generalViewModel.getAllCuentasC()
            generalViewModel.dataGetAllCuentasC.observeOnce(viewLifecycleOwner){ listaCuentasB->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralCuentaEfectivoValue.text.toString(),
                    listaCuentasB.map { it.AcctName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralCuentaEfectivoValue.text = opcionElegida
                    infoUsuario["acctEfectivo"] = listaCuentasB[id].AcctCode.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }

        //Cuenta Transferencia
        binding.clUsuarioGeneralCuentaTransferencia.setOnClickListener {
            generalViewModel.getAllCuentasC()
            generalViewModel.dataGetAllCuentasC.observeOnce(viewLifecycleOwner){ listaCuentasB->
                BaseDialogCheckListWithViewAndId(
                    requireActivity(),
                    binding.txvUsuarioGeneralCuentaTransferenciaValue.text.toString(),
                    listaCuentasB.map { it.AcctName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralCuentaTransferenciaValue.text = opcionElegida
                    infoUsuario["acctTransferencia"] = listaCuentasB[id].AcctCode.toString()
                }.show(requireActivity().supportFragmentManager, "showDialog")
            }
        }
    }


    private fun setNoSelectableContainers(){
        val seleccionable = false
        binding.clUsuarioGeneralCodigo.isClickable = seleccionable
        /*binding.clUsuarioGeneralPassword.isClickable = seleccionable*/
        binding.clUsuarioGeneralIDTelefono.isClickable = seleccionable
        binding.clUsuarioGeneralNombre.isClickable = seleccionable
        binding.clUsuarioGeneralCorreo.isClickable = seleccionable
        binding.clUsuarioGeneralTelefono.isClickable = seleccionable
        binding.clUsuarioGeneralRRHHEmpleado.isClickable = seleccionable
        binding.clUsuarioGeneralVendedor.isClickable = seleccionable
        binding.clUsuarioGeneralSerieClientes.isClickable = seleccionable
        binding.clUsuarioGeneralSeriePedidos.isClickable = seleccionable
        binding.clUsuarioGeneralSeriePagos.isClickable = seleccionable
        binding.clUsuarioGeneralListaPrecio.isClickable = seleccionable
        binding.clUsuarioGeneralImpuesto.isClickable = seleccionable
        binding.clUsuarioGeneralMoneda.isClickable = seleccionable
        binding.clUsuarioGeneralProyecto.isClickable = seleccionable
        binding.clUsuarioGeneralAlmacen.isClickable = seleccionable
        binding.clUsuarioGeneralCuentaEfectivo.isClickable = seleccionable
        binding.clUsuarioGeneralCuentaTransferencia.isClickable = seleccionable
        binding.clUsuarioGeneralCuentaDeposito.isClickable = seleccionable
        binding.clUsuarioGeneralCuentaCheque.isClickable = seleccionable
        binding.clUsuarioGeneralSuperUsuario.isClickable = seleccionable
        binding.clUsuarioGeneralActivo.isClickable = seleccionable
        binding.clUsuarioGeneralEdicionPrecios.isClickable = seleccionable
        binding.clUsuarioGeneralPuedeCrear.isClickable = seleccionable
        binding.clUsuarioGeneralPuedeActualizar.isClickable = seleccionable
        binding.clUsuarioGeneralPuedeDeclinar.isClickable = seleccionable
        binding.clUsuarioGeneralPuedeAprovar.isClickable = seleccionable
        binding.clUsuarioGeneralSesionActiva.isClickable = seleccionable

        //CheckBoxes
        binding.cbUsuarioSuperUsuario.isClickable = seleccionable
        binding.cbUsuarioActivo.isClickable = seleccionable
        binding.cbUsuarioEdicionPrecios.isClickable = seleccionable
        binding.cbUsuarioPuedeCrear.isClickable = seleccionable
        binding.cbUsuarioPuedeActualizar.isClickable = seleccionable
        binding.cbUsuarioPuedeDeclinar.isClickable = seleccionable
        binding.cbUsuarioPuedeAprovar.isClickable = seleccionable
        binding.cbUsuarioSesionActiva.isClickable = seleccionable
    }


    private fun hideImages() {
        val mostrar = false
        binding.imvCodigo.isVisible = mostrar
        /*binding.imvPassword.isVisible = mostrar*/
        binding.imvIDTelefono.isVisible = mostrar
        binding.imvNombre.isVisible = mostrar
        binding.imvCorreo.isVisible = mostrar
        binding.imvConductor.isVisible = mostrar
        binding.imvTelefono.isVisible = mostrar
        binding.imvRRHHEmpleado.isVisible = mostrar
        binding.imvVendedor.isVisible = mostrar
        binding.imvSerieClientes.isVisible = mostrar
        binding.imvSeriePedidos.isVisible = mostrar
        binding.imvSeriePagos.isVisible = mostrar
        binding.imvListaPrecio.isVisible = mostrar
        binding.imvImpuesto.isVisible = mostrar
        binding.imvMoneda.isVisible = mostrar
        binding.imvProyecto.isVisible = mostrar
        binding.imvAlmacen.isVisible = mostrar
        binding.imvCuentaEfectivo.isVisible = mostrar
        binding.imvCuentaTransferencia.isVisible = mostrar
        binding.imvCuentaDeposito.isVisible = mostrar
        binding.imvCuentaCheque.isVisible = mostrar
    }


    fun getUserInfo(): DoConfigurarUsuario{
        return DoConfigurarUsuario(
            Code = binding.txvUsuarioGeneralCodigoValue.text.toString(),
            Password = binding.txvUsuarioGeneralPasswordValue.text.toString(),
            IdPhone1 = binding.txvUsuarioGeneralIDTelefonoValue.text.toString(),

            Name = binding.txvUsuarioGeneralNombreValue.text.toString(),
            Email = binding.txvUsuarioGeneralCorreoValue.text.toString(),
            Phone1 = binding.txvUsuarioGeneralTelefonoValue.text.toString(),
            DefaultSlpCode = infoUsuario["slpCode"].toString(),

            DefaultOrderSeries = infoUsuario["pedidoSeries"].toString(),
            DefaultSNSerieCli = infoUsuario["socioSeries"].toString(),
            DefaultPagoRSeries = infoUsuario["pagosSeries"].toString(),
            DefaultPriceList = infoUsuario["priceList"].toString(),
            DefaultWarehouse = infoUsuario["whsCode"].toString(),
            DefaultCurrency = infoUsuario["currCode"].toString(),
            DefaultTaxCode = infoUsuario["taxCode"].toString(),
            DefaultProyecto = "",

            DefaultAcctCodeCh = "",
            DefaultAcctCodeDe = "",
            DefaultAcctCodeEf = infoUsuario["acctEfectivo"].toString(),
            DefaultAcctCodeTr = infoUsuario["acctTransferencia"].toString(),
            DefaultConductor = "",

            DefaultZona = "",

            AccStatusSession = binding.cbUsuarioSesionActiva.isChecked.getStringBool(),
            SuperUser = binding.cbUsuarioSuperUsuario.isChecked.getStringForAccLocked(),
            AccLocked = binding.cbUsuarioActivo.isChecked.getStringBool(),
            CanEditPrice = binding.cbUsuarioEdicionPrecios.isChecked.getStringBool(),
            CanCreate = binding.cbUsuarioPuedeCrear.isChecked.getStringBool(),
            CanUpdate = binding.cbUsuarioPuedeActualizar.isChecked.getStringBool(),
            CanDecline = binding.cbUsuarioPuedeDeclinar.isChecked.getStringBool(),
            CanApprove = binding.cbUsuarioPuedeAprovar.isChecked.getStringBool(),
            ListaPrecios = 0,
            GrupoArticulos = 0,
            GrupoSocios = 0,
            Almacenes = 0,
            Zonas = 0,
        )
    }

    private fun validateEmptyFields():String{
        val emptyFields = mutableListOf<String>()
         if (binding.txvUsuarioGeneralCodigoValue.text.toString().isEmpty())emptyFields.add("\n Codigo")
         if (binding.txvUsuarioGeneralPasswordValue.text.toString().isEmpty())emptyFields.add("\n Password")
         if (binding.txvUsuarioGeneralIDTelefonoValue.text.toString().isEmpty())emptyFields.add("\n IDTelefono")
         if (binding.txvUsuarioGeneralNombreValue.text.toString().isEmpty())emptyFields.add("\n Nombre")
         if (binding.txvUsuarioGeneralCorreoValue.text.toString().isEmpty())emptyFields.add("\n Correo")
         if (binding.txvUsuarioGeneralTelefonoValue.text.toString().isEmpty())emptyFields.add("\n Telefono")
         if (binding.txvUsuarioGeneralVendedorValue.text.toString().isEmpty())emptyFields.add("\n Vendedor")
         if (binding.txvUsuarioGeneralSerieClientesValue.text.toString().isEmpty())emptyFields.add("\n SerieClientes")
         if (binding.txvUsuarioGeneralSeriePedidosValue.text.toString().isEmpty())emptyFields.add("\n SeriePedidos")
         if (binding.txvUsuarioGeneralSeriePagosValue.text.toString().isEmpty())emptyFields.add("\n SeriePagos")
         if (binding.txvUsuarioGeneralListaPrecioValue.text.toString().isEmpty())emptyFields.add("\n ListaPrecio")
         if (binding.txvUsuarioGeneralImpuestoValue.text.toString().isEmpty())emptyFields.add("\n Impuesto")
         if (binding.txvUsuarioGeneralMonedaValue.text.toString().isEmpty())emptyFields.add("\n Moneda")
         if (binding.txvUsuarioGeneralAlmacenValue.text.toString().isEmpty())emptyFields.add("\n Almacen")
         if (binding.txvUsuarioGeneralCuentaEfectivoValue.text.toString().isEmpty())emptyFields.add("\n CuentaEfectivo")
         if (binding.txvUsuarioGeneralCuentaTransferenciaValue.text.toString().isEmpty())emptyFields.add("\n CuentaTransferencia")
        return emptyFields.joinToString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}