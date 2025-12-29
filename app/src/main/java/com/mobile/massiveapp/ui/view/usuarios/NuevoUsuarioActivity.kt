package com.mobile.massiveapp.ui.view.usuarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityNuevoUsuarioBinding
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.ui.base.BaseDialogCheckListWithViewAndId
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.base.BaseDialogUsuarioInfo
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.util.getBoolByYorN
import com.mobile.massiveapp.ui.view.util.getBoolFromAccLocked
import com.mobile.massiveapp.ui.view.util.getStringBool
import com.mobile.massiveapp.ui.view.util.getStringForAccLocked
import com.mobile.massiveapp.ui.viewmodel.ArticuloViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuariosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NuevoUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuevoUsuarioBinding
    private lateinit var loadinDialog: BaseDialogSImpleLoading
    private val generalViewModel: GeneralViewModel by viewModels()
    private val articulosViewModel: ArticuloViewModel by viewModels()
    private val usuariosViewModel: UsuariosViewModel by viewModels()

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private var accion: String = "I"

    private var listaDatosUsuario = HashMap<String, List<DoNuevoUsuarioItem>>()

    private var infoUsuario = HashMap<String, Any>()
    private val OBJ_CODE_CLIENTES = 2
    private val OBJ_CODE_PEDIDOS = 17
    private val OBJ_CODE_PAGOS = 24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadinDialog = BaseDialogSImpleLoading(this)

        //Si es edicion
        if (intent.getBooleanExtra("edicionUsuario", false)){
            binding.imvCodigo.isVisible = false
            binding.clUsuarioGeneralCodigo.isClickable = false

            accion = "U"

            title = "Editar Usuario"
        }

        try {
            liveDataInfoUsuario()
            setValoresIniciales()
            setDefaultUi()
        } catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }



    private fun liveDataInfoUsuario() {
        //Zonas
        usuariosViewModel.dataGetAllUsuarioZonasCreacion.observe(this){ zonas->
            binding.txvUsuarioGeneralZonasValue.text = "${zonas.filter { it.Checked }.size} zonas"
            listaDatosUsuario["Zonas"] = zonas
        }
        //Lista precios
        usuariosViewModel.dataGetAllUsuarioListaPreciosCreacion.observe(this){ listasPrecios->
            binding.txvUsuarioGeneralListaPreciosValue.text = "${listasPrecios.filter { it.Checked }.size} listas de precio"
            listaDatosUsuario["ListaPrecios"] = listasPrecios
        }
        //Almacenes
        usuariosViewModel.dataGetAllUsuarioAlmacenesCreacion.observe(this){ almacenes->
            binding.txvUsuarioGeneralAlmacenesListaValue.text = "${almacenes.filter { it.Checked }.size} almacenes"
            listaDatosUsuario["Almacenes"] = almacenes
        }
        //Grupo articulos
        usuariosViewModel.dataGetAllUsuarioGrupoArticulosCreacion.observe(this){ grupoArticulos->
            binding.txvUsuarioGeneralGruposArticulosValue.text = "${grupoArticulos.filter { it.Checked }.size} grupos de artículo"
            listaDatosUsuario["GrupoArticulos"] = grupoArticulos
        }
        //Grupo socios
        usuariosViewModel.dataGetAllUsuarioGrupoSociosCreacion.observe(this){ grupoSocios->
            binding.txvUsuarioGeneralGruposSociosValue.text = "${grupoSocios.filter { it.Checked }.size} grupos de socios"
            listaDatosUsuario["GrupoSocios"] = grupoSocios
        }

        //Send Usuario Response
        usuariosViewModel.dataSendUsuario.observe(this){ response->
            loadinDialog.onDismiss()

            when(response.ErrorCodigo){
                500 ->{ usuarioViewModel.logOut() }
                0 -> {
                    showMessage(response.ErrorMensaje)
                    onBackPressedDispatcher.onBackPressed()
                }
                else -> { showMessage(response.ErrorMensaje) }
            }
        }

        val usuarioCode = intent.getStringExtra("usuarioCode")?:""

            //Usuario edicion
        usuariosViewModel.dataGetAllUsuarioGeneralInfo.observe(this){ infoUsuarioObj->

            if (!intent.getBooleanExtra("usuarioDuplicado", false)){
                binding.txvUsuarioGeneralCodigoValue.text = infoUsuarioObj.Code
                binding.txvUsuarioGeneralPasswordValue.text = infoUsuarioObj.Password
                binding.txvUsuarioGeneralIDTelefonoValue.text = infoUsuarioObj.IdPhone1
                binding.txvUsuarioGeneralNombreValue.text = infoUsuarioObj.Name
                binding.txvUsuarioGeneralCorreoValue.text = infoUsuarioObj.Email
                binding.txvUsuarioGeneralTelefonoValue.text = infoUsuarioObj.Phone1
                binding.txvUsuarioGeneralVendedorValue.text = infoUsuarioObj.NombreVendedor
            }
            binding.txvUsuarioGeneralSerieClientesValue.text = infoUsuarioObj.SerieSocio
            binding.txvUsuarioGeneralSeriePedidosValue.text = infoUsuarioObj.SeriePedido
            binding.txvUsuarioGeneralSeriePagosValue.text = infoUsuarioObj.SeriePago
            binding.txvUsuarioGeneralListaPrecioValue.text = infoUsuarioObj.ListaPrecio
            binding.txvUsuarioGeneralImpuestoValue.text = infoUsuarioObj.Impuesto
            binding.txvUsuarioGeneralMonedaValue.text = infoUsuarioObj.Moneda
            binding.txvUsuarioGeneralProyectoValue.text = infoUsuarioObj.Proyecto
            binding.txvUsuarioGeneralAlmacenValue.text = infoUsuarioObj.Almacen
            binding.txvUsuarioGeneralCuentaEfectivoValue.text = infoUsuarioObj.CuentaEfectivo
            binding.txvUsuarioGeneralCuentaTransferenciaValue.text = infoUsuarioObj.CuentaTransferencia
            binding.txvUsuarioGeneralCuentaDepositoValue.text = infoUsuarioObj.CuentaDeposito
            binding.txvUsuarioGeneralCuentaChequeValue.text = infoUsuarioObj.CuentaCheque
            binding.txvUsuarioGeneralZonaUsuarioValue.text = infoUsuarioObj.DefaultZona

            binding.cbUsuarioActivo.isChecked = infoUsuarioObj.AccLocked.getBoolFromAccLocked()
            binding.cbUsuarioSuperUsuario.isChecked = infoUsuarioObj.SuperUser.getBoolByYorN()
            binding.cbUsuarioEdicionPrecios.isChecked = infoUsuarioObj.CanEditPrice.getBoolByYorN()
            binding.cbUsuarioPuedeCrear.isChecked = infoUsuarioObj.CanCreate.getBoolByYorN()
            binding.cbUsuarioPuedeActualizar.isChecked = infoUsuarioObj.CanUpdate.getBoolByYorN()
            binding.cbUsuarioPuedeDeclinar.isChecked = infoUsuarioObj.CanDecline.getBoolByYorN()
            binding.cbUsuarioPuedeAprovar.isChecked = infoUsuarioObj.CanApprove.getBoolByYorN()
            binding.cbUsuarioSesionActiva.isChecked = infoUsuarioObj.AccStatusSession.getBoolByYorN()

            infoUsuario["acctCheque"] = infoUsuarioObj.DefaultAcctCodeCh
            infoUsuario["acctDeposito"] = infoUsuarioObj.DefaultAcctCodeDe
            infoUsuario["acctEfectivo"] = infoUsuarioObj.DefaultAcctCodeEf
            infoUsuario["acctTransferencia"] = infoUsuarioObj.DefaultAcctCodeTr

            infoUsuario["pedidoSeries"] = infoUsuarioObj.DefaultOrderSeries
            infoUsuario["socioSeries"] = infoUsuarioObj.DefaultSNSerieCli
            infoUsuario["pagosSeries"] = infoUsuarioObj.DefaultPagoRSeries
            infoUsuario["priceList"] = infoUsuarioObj.DefaultPriceList
            infoUsuario["whsCode"] = infoUsuarioObj.DefaultWarehouse
            infoUsuario["currCode"] = infoUsuarioObj.DefaultCurrency
            infoUsuario["taxCode"] = infoUsuarioObj.DefaultTaxCode
            infoUsuario["slpCode"] = infoUsuarioObj.DefaultSlpCode
            infoUsuario["zonaUsuario"] = infoUsuarioObj.DefaultZona

            setInfoNuevoUsuario(nuevoUsuario = usuarioCode.isNotEmpty())
        }

        usuariosViewModel.dataValidarCreacionUsuario.observe(this){ usuarioValidacion->
            val userValidation =
                if (usuarioValidacion.Email){
                "Email"
                } else if (usuarioValidacion.Codigo){
                    "Codigo"
                } else if (usuarioValidacion.IdTelefono){
                    "ID Teléfono"
                } else if (usuarioValidacion.Vendedor){
                    "Vendedor"
                } else if (usuarioValidacion.Telefono){
                    "Número de teléfono"
                } else if (usuarioValidacion.Nombre){
                    "Nombre"
                } else {
                    ""
                }

            if (userValidation.isNotEmpty()){
                Toast.makeText(this, "El $userValidation ya esta en uso", Toast.LENGTH_SHORT).show()
            } else{
                loadinDialog.startLoading()
                usuariosViewModel.sendUsuario(
                    datosUsuario = listaDatosUsuario,
                    usuarioCabecera = getUsuarioCabecera(),
                    accion = accion
                )
            }
        }

        cierreSesion()
    }


    private fun cierreSesion() {
        //LiveData Cierre de Sesion
        usuarioViewModel.isLoadingLogOut.observe(this){
            val loadingSimpleDialog = BaseDialogSImpleLoading(this)
            loadingSimpleDialog.startLoading("Cerrando Sesion...")

            if (!it){
                loadingSimpleDialog.onDismiss()
            }
        }
        usuarioViewModel.dataLogOut.observe(this) { error->
            if (error.ErrorCodigo == 0){
                Intent(this, LoginActivity::class.java).also { startActivity(it) }
                finish()
            } else {
                Toast.makeText(this, error.ErrorMensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

/*
    private fun setInfoUsuario(infoUsuario: DoConfigurarUsuario) {

        binding.txvUsuarioGeneralSerieClientesValue.text = infoUsuario.DefaultSNSerieCli
        binding.txvUsuarioGeneralSeriePedidosValue.text = infoUsuario.DefaultOrderSeries
        binding.txvUsuarioGeneralSeriePagosValue.text = infoUsuario.DefaultPagoRSeries
        binding.txvUsuarioGeneralListaPrecioValue.text = infoUsuario.DefaultPriceList
        binding.txvUsuarioGeneralImpuestoValue.text = infoUsuario.DefaultTaxCode
        binding.txvUsuarioGeneralMonedaValue.text = infoUsuario.DefaultCurrency
        binding.txvUsuarioGeneralProyectoValue.text = infoUsuario.DefaultProyecto
        binding.txvUsuarioGeneralAlmacenValue.text = infoUsuario.DefaultWarehouse
        binding.txvUsuarioGeneralCuentaEfectivoValue.text = infoUsuario.DefaultAcctCodeEf
        binding.txvUsuarioGeneralCuentaTransferenciaValue.text = infoUsuario.DefaultAcctCodeTr
        binding.txvUsuarioGeneralCuentaDepositoValue.text = infoUsuario.DefaultAcctCodeDe
        binding.txvUsuarioGeneralCuentaChequeValue.text = infoUsuario.DefaultAcctCodeCh

        //Vendedor
        generalViewModel.dataGetAllGeneralVendedores.observe(this){ listaVendedores->
            binding.txvUsuarioGeneralVendedorValue.text = infoUsuario.DefaultSlpCode
        }

        //Series - Clientes
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_CLIENTES }

            }
        }

        //Series - Pedidos
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
            val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PEDIDOS }
            }
        }

        //Series - Pagos
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
            val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PAGOS }
            }
        }

        //Lista Precios
        articulosViewModel.dataGetAllArticuloListaPrecios.observe(this){ listaPrecios->

        }

        //Impuesto
        generalViewModel.dataGetAllGeneralImpuestos.observe(this){ listaImpuestos->

        }

        //Monedas
        generalViewModel.dataGetAllGeneralMonedas.observe(this){ listaMonedas->

        }

        //Almacenes
        articulosViewModel.dataGetAllArticuloAlmacenes.observe(this){ listaAlmacenes->

        }

        //Cuenta Efectivo
        generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->

        }

        //Cuenta Transferencia
        generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->

        }

        //Cuenta Deposito
        generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->

        }

        //Cuenta Cheque
        generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->

        }
    }
*/

    private fun setValoresIniciales() {
        val usuarioCode = intent.getStringExtra("usuarioCode")?:""
        val duplicado = intent.getBooleanExtra("usuarioDuplicado", false)

        if (usuarioCode.isEmpty() || duplicado){
            binding.clUsuarioGeneralSesionActiva.isVisible = false
        }

        usuariosViewModel.getAllUsuarioGeneralInfo(usuarioCode)
        usuariosViewModel.getAllUsuarioZonasCreacion(usuarioCode)
        usuariosViewModel.getAllUsuarioListaPreciosCreacion(usuarioCode)
        usuariosViewModel.getAllUsuarioAlmacenesCreacion(usuarioCode)
        usuariosViewModel.getAllUsuarioGrupoArticulosCreacion(usuarioCode)
        usuariosViewModel.getAllUsuarioGrupoSociosCreacion(usuarioCode)

        generalViewModel.getAllZonas()
        generalViewModel.getAllGeneralVendedores()
        generalViewModel.getAllSeriesN()
        generalViewModel.getAllConductores() //CONDUCTORES
        articulosViewModel.getAllArticuloListaPrecios()
        generalViewModel.getAllGeneralImpuestos()
        generalViewModel.getAllGeneralMonedas()
        articulosViewModel.getAllArticuloAlmacenes()
        generalViewModel.getAllCuentasC()
    }

    private fun setInfoNuevoUsuario(nuevoUsuario: Boolean) {

        //Vendedor
        generalViewModel.dataGetAllGeneralVendedores.observe(this){ listaVendedores->
            if (listaVendedores.isNotEmpty()){
                if (listaVendedores.size == 1 && nuevoUsuario){
                    binding.txvUsuarioGeneralVendedorValue.text = listaVendedores.first().SlpName
                    infoUsuario["slpCode"] = listaVendedores.first().SlpCode.toString()
                }
            }
        }

        //Series
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
                val listaSeriesSociosFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_CLIENTES }
                val listaSeriesPagosFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PAGOS }
                val listaSeriesPedidosFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PEDIDOS }

                //Series - Clientes
                if (listaSeriesSociosFiltrada.size == 1){
                    val serieClienteDefault = listaSeriesSociosFiltrada.filter { it.Series == 1 }
                    binding.txvUsuarioGeneralSerieClientesValue.text = serieClienteDefault.first().SeriesName
                    infoUsuario["socioSeries"] = serieClienteDefault.first().Series.toString()
                }

                //Series - Pedidos
                if (listaSeriesPedidosFiltrada.size == 1){
                    binding.txvUsuarioGeneralSeriePedidosValue.text = listaSeriesPedidosFiltrada.first().SeriesName
                    infoUsuario["pedidoSeries"] = listaSeriesPedidosFiltrada.first().Series.toString()
                }

                //Series - Pagos
                if (listaSeriesPagosFiltrada.size == 1){
                    binding.txvUsuarioGeneralSeriePagosValue.text = listaSeriesPagosFiltrada.first().SeriesName
                    infoUsuario["pagosSeries"] = listaSeriesPagosFiltrada.first().Series.toString()
                }
            }
        }
/*
        //Series - Pedidos
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PEDIDOS }
                if (listaSeriesFiltrada.size == 1){
                    binding.txvUsuarioGeneralSeriePedidosValue.text = listaSeriesFiltrada.first().SeriesName
                    infoUsuario["pedidoSeries"] = listaSeriesFiltrada.first().Series.toString()
                }
            }
        }

        //Series - Pagos
        generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
            if (listaSeries.isNotEmpty()){
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PAGOS }
                if (listaSeriesFiltrada.size == 1){
                    binding.txvUsuarioGeneralSeriePagosValue.text = listaSeriesFiltrada.first().SeriesName
                    infoUsuario["pagosSeries"] = listaSeriesFiltrada.first().Series.toString()
                }
            }
        }*/

        //Lista Precios
        articulosViewModel.dataGetAllArticuloListaPrecios.observe(this){ listaPrecios->
            if (listaPrecios.isNotEmpty()){
                if (listaPrecios.size == 1){
                    binding.txvUsuarioGeneralListaPrecioValue.text = listaPrecios.first().ListName
                    infoUsuario["priceList"] = listaPrecios.first().ListNum.toString()
                }
            }
        }

        //Impuesto
        generalViewModel.dataGetAllGeneralImpuestos.observe(this){ listaImpuestos->
            if (listaImpuestos.isNotEmpty()){
                if (listaImpuestos.size == 1){
                    binding.txvUsuarioGeneralImpuestoValue.text = listaImpuestos.first().Name
                    infoUsuario["taxCode"] = listaImpuestos.first().Code
                }
            }
        }

        //Monedas
        generalViewModel.dataGetAllGeneralMonedas.observe(this){ listaMonedas->
            if (listaMonedas.isNotEmpty()){
                if (listaMonedas.size == 1){
                    binding.txvUsuarioGeneralMonedaValue.text = listaMonedas.first().CurrCode
                    infoUsuario["currCode"] = listaMonedas.first().CurrCode.toString()
                }
            }
        }

        //Almacenes
        articulosViewModel.dataGetAllArticuloAlmacenes.observe(this){ listaAlmacenes->
            if (listaAlmacenes.isNotEmpty()){
                if (listaAlmacenes.size == 1){
                    binding.txvUsuarioGeneralAlmacenValue.text = listaAlmacenes.first().WhsName
                    infoUsuario["whsCode"] = listaAlmacenes.first().WhsCode
                }
            }
        }

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

        //Almacenes
        binding.clUsuarioGeneralAlmacenesLista.setOnClickListener {
            BaseDialogUsuarioInfo(
                listaDatosUsuario["Almacenes"]?: emptyList(),
                "Almacenes"
            ){ listaAlmacenes->
                listaDatosUsuario["Almacenes"] = listaAlmacenes
                binding.txvUsuarioGeneralAlmacenesListaValue.text = "${listaAlmacenes.filter { it.Checked }.size} almacenes"
            }.show(supportFragmentManager, "showDialog")
        }

        //Lista Precios
        binding.clUsuarioGeneralListaPrecios.setOnClickListener {
            BaseDialogUsuarioInfo(
                listaDatosUsuario["ListaPrecios"]?: emptyList(),
                "Lista de Precios"
            ){ listaPrecios->
                listaDatosUsuario["ListaPrecios"] = listaPrecios
                binding.txvUsuarioGeneralListaPreciosValue.text = "${listaPrecios.filter { it.Checked }.size} listas de precio"
            }.show(supportFragmentManager, "showDialog")
        }

        //Lista grupo articulos
        binding.clUsuarioGeneralGruposArticulos.setOnClickListener {
            BaseDialogUsuarioInfo(
                listaDatosUsuario["GrupoArticulos"]?: emptyList(),
                "Grupo de Artículos"
            ){ grupoArticulos->
                listaDatosUsuario["GrupoArticulos"] = grupoArticulos
                binding.txvUsuarioGeneralGruposArticulosValue.text = "${grupoArticulos.filter { it.Checked }.size} grupos de artículo"
            }.show(supportFragmentManager, "showDialog")
        }

        //Lista grupo socios
        binding.clUsuarioGeneralGruposSocios.setOnClickListener {
            BaseDialogUsuarioInfo(
                listaDatosUsuario["GrupoSocios"]?: emptyList(),
                "Grupo de Socios"
            ){ grupoSocios->
                listaDatosUsuario["GrupoSocios"] = grupoSocios
                binding.txvUsuarioGeneralGruposSociosValue.text = "${grupoSocios.filter { it.Checked }.size} grupos de socios"
            }.show(supportFragmentManager, "showDialog")
        }

        //Lista de zonas
        binding.clUsuarioGeneralZonas.setOnClickListener {
            BaseDialogUsuarioInfo(
                listaDatosUsuario["Zonas"]?: emptyList(),
                "Zonas"
            ){ listaZonas->
                listaDatosUsuario["Zonas"] = listaZonas
                binding.txvUsuarioGeneralZonasValue.text = "${listaZonas.filter { it.Checked }.size} zonas"
            }.show(supportFragmentManager, "showDialog")
        }


        //Codigo
        binding.clUsuarioGeneralCodigo.setOnClickListener {
            if (!intent.getBooleanExtra("edicionUsuario", false)){
                BaseDialogEdtWithTypeEdt(
                    "text",
                    binding.txvUsuarioGeneralCodigoValue.text.toString(),
                    "Ingrese el Código"
                ){ newText->
                    binding.txvUsuarioGeneralCodigoValue.text = newText
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Password
        binding.clUsuarioGeneralPassword.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralPasswordValue.text.toString(),
                "Ingrese la contraseña"
            ){ newText->
                binding.txvUsuarioGeneralPasswordValue.text = newText
            }.show(supportFragmentManager, "showDialog")
        }

        //Conductor
        binding.clUsuarioGeneralConductor.setOnClickListener {
            generalViewModel.dataGetAllConductores.observe(this){ listaConductores->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralConductorValue.text.toString(),
                    listaConductores.map { it.Name },
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralConductorValue.text = opcionElegida
                    infoUsuario["conductor"] = listaConductores[id].Code
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Id Movil
        binding.clUsuarioGeneralIDTelefono.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralIDTelefonoValue.text.toString(),
                "Ingrese el ID del Teléfono"
            ){ newText->
                binding.txvUsuarioGeneralIDTelefonoValue.text = newText
            }.show(supportFragmentManager, "showDialog")
        }

        //Nombre
        binding.clUsuarioGeneralNombre.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralNombreValue.text.toString(),
                "Ingrese el Nombre"
            ){ newText->
                binding.txvUsuarioGeneralNombreValue.text = newText
            }.show(supportFragmentManager, "showDialog")
        }

        //Correo
        binding.clUsuarioGeneralCorreo.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralCorreoValue.text.toString(),
                "Ingrese el Correo"
            ){ newText->
                binding.txvUsuarioGeneralCorreoValue.text = newText
            }.show(supportFragmentManager, "showDialog")
        }

        //Telefono
        binding.clUsuarioGeneralTelefono.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "text",
                binding.txvUsuarioGeneralTelefonoValue.text.toString(),
                "Ingrese el Teléfono"
            ){ newText->
                binding.txvUsuarioGeneralTelefonoValue.text = newText
            }.show(supportFragmentManager, "showDialog")
        }

        //Vendedor
        binding.clUsuarioGeneralVendedor.setOnClickListener {
            generalViewModel.dataGetAllGeneralVendedores.observe(this){ listaVendedores->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralVendedorValue.text.toString(),
                    listaVendedores.map { it.SlpName },
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralVendedorValue.text = opcionElegida
                    infoUsuario["slpCode"] = listaVendedores[id].SlpCode.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Zona Usuario
        binding.clUsuarioGeneralZonaUsuario.setOnClickListener {
            generalViewModel.dataGetAllZonas.observe(this){ listaZonas->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralZonaUsuarioValue.text.toString(),
                    listaZonas.map { it.Name },
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralZonaUsuarioValue.text = opcionElegida
                    infoUsuario["zonaUsuario"] = listaZonas[id].Code
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Series - Clientes
        binding.clUsuarioGeneralSerieClientes.setOnClickListener {
            generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_CLIENTES }
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralSerieClientesValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSerieClientesValue.text = opcionElegida
                    infoUsuario["pedidoSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Series - Pedidos
        binding.clUsuarioGeneralSeriePedidos.setOnClickListener {
            generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PEDIDOS }
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralSeriePedidosValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSeriePedidosValue.text = opcionElegida
                    infoUsuario["socioSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Series - Pagos
        binding.clUsuarioGeneralSeriePagos.setOnClickListener {
            generalViewModel.dataGetAllSeriesN.observe(this){ listaSeries->
                val listaSeriesFiltrada = listaSeries.filter { it.ObjectCode == OBJ_CODE_PAGOS }
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralSeriePagosValue.text.toString(),
                    listaSeriesFiltrada.map { it.SeriesName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralSeriePagosValue.text = opcionElegida
                    infoUsuario["pagosSeries"] = listaSeriesFiltrada[id].Series.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Lista Precios
        binding.clUsuarioGeneralListaPrecio.setOnClickListener {
            articulosViewModel.dataGetAllArticuloListaPrecios.observe(this){ listaPrecios->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralListaPrecioValue.text.toString(),
                    listaPrecios.map { it.ListName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralListaPrecioValue.text = opcionElegida
                    infoUsuario["priceList"] = listaPrecios[id].ListNum.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Impuesto
        binding.clUsuarioGeneralImpuesto.setOnClickListener {
            generalViewModel.dataGetAllGeneralImpuestos.observe(this){ listaImpuestos->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralImpuestoValue.text.toString(),
                    listaImpuestos.map { it.Name }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralImpuestoValue.text = opcionElegida
                    infoUsuario["taxCode"] = listaImpuestos[id].Code
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Monedas
        binding.clUsuarioGeneralMoneda.setOnClickListener {
            generalViewModel.dataGetAllGeneralMonedas.observe(this){ listaMonedas->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralMonedaValue.text.toString(),
                    listaMonedas.map { it.CurrCode }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralMonedaValue.text = opcionElegida
                    infoUsuario["currCode"] = listaMonedas[id].CurrCode.toString()
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Proyectos
        binding.clUsuarioGeneralProyecto.setOnClickListener {
        }

        //Almacenes
        binding.clUsuarioGeneralAlmacen.setOnClickListener {
            articulosViewModel.dataGetAllArticuloAlmacenes.observe(this){ listaAlmacenes->
                BaseDialogCheckListWithViewAndId(
                    this,
                    binding.txvUsuarioGeneralAlmacenValue.text.toString(),
                    listaAlmacenes.map { it.WhsName }
                ){ opcionElegida, id->
                    binding.txvUsuarioGeneralAlmacenValue.text = opcionElegida
                    infoUsuario["whsCode"] = listaAlmacenes[id].WhsCode
                }.show(supportFragmentManager, "showDialog")
            }
        }

        //Cuenta Efectivo
        binding.clUsuarioGeneralCuentaEfectivo.setOnClickListener {
            generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->
                if (listaCuentasB.isNotEmpty()){
                    BaseDialogCheckListWithViewAndId(
                        this,
                        binding.txvUsuarioGeneralCuentaEfectivoValue.text.toString(),
                        listaCuentasB.map { it.AcctName }
                    ){ opcionElegida, id->
                        binding.txvUsuarioGeneralCuentaEfectivoValue.text = opcionElegida
                        infoUsuario["acctEfectivo"] = listaCuentasB[id].AcctCode.toString()
                    }.show(supportFragmentManager, "showDialog")
                } else {
                    showMessage("No hay información")
                }
            }
        }

        //Cuenta Transferencia
        binding.clUsuarioGeneralCuentaTransferencia.setOnClickListener {
            generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->
                if (listaCuentasB.isNotEmpty()){
                    BaseDialogCheckListWithViewAndId(
                        this,
                        binding.txvUsuarioGeneralCuentaTransferenciaValue.text.toString(),
                        listaCuentasB.map { it.AcctName }
                    ){ opcionElegida, id->
                        binding.txvUsuarioGeneralCuentaTransferenciaValue.text = opcionElegida
                        infoUsuario["acctTransferencia"] = listaCuentasB[id].AcctCode.toString()
                    }.show(supportFragmentManager, "showDialog")
                } else {
                    showMessage("No hay información")
                }
            }
        }

        //Cuenta Deposito
        binding.clUsuarioGeneralCuentaDeposito.setOnClickListener {
            generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->
                if (listaCuentasB.isNotEmpty()){

                    BaseDialogCheckListWithViewAndId(
                        this,
                        binding.txvUsuarioGeneralCuentaDepositoValue.text.toString(),
                        listaCuentasB.map { it.AcctName }
                    ){ opcionElegida, id->
                        binding.txvUsuarioGeneralCuentaDepositoValue.text = opcionElegida
                        infoUsuario["acctDeposito"] = listaCuentasB[id].AcctCode.toString()
                    }.show(supportFragmentManager, "showDialog")
                } else{
                    showMessage("No hay información")
                }
            }
        }

        //Cuenta Cheque
        binding.clUsuarioGeneralCuentaCheque.setOnClickListener {
            generalViewModel.dataGetAllCuentasC.observe(this){ listaCuentasB->
                if (listaCuentasB.isNotEmpty()){
                    BaseDialogCheckListWithViewAndId(
                        this,
                        binding.txvUsuarioGeneralCuentaChequeValue.text.toString(),
                        listaCuentasB.map { it.AcctName }
                    ){ opcionElegida, id->
                        binding.txvUsuarioGeneralCuentaChequeValue.text = opcionElegida
                        infoUsuario["acctCheque"] = listaCuentasB[id].AcctCode.toString()
                    }.show(supportFragmentManager, "showDialog")
                } else {
                    showMessage("No hay información")
                }
            }
        }
    }

    private fun getUsuarioCabecera(): DoConfigurarUsuario{
        return DoConfigurarUsuario(
            Code = binding.txvUsuarioGeneralCodigoValue.text.toString(),
            Password = binding.txvUsuarioGeneralPasswordValue.text.toString(),
            IdPhone1 = binding.txvUsuarioGeneralIDTelefonoValue.text.toString(),

            Name = binding.txvUsuarioGeneralNombreValue.text.toString(),
            Email = binding.txvUsuarioGeneralCorreoValue.text.toString(),
            Phone1 = binding.txvUsuarioGeneralTelefonoValue.text.toString(),
            DefaultSlpCode = infoUsuario["slpCode"].toString(),

            DefaultOrderSeries =    infoUsuario["pedidoSeries"].toString(),
            DefaultSNSerieCli = infoUsuario["socioSeries"].toString(),
            DefaultPagoRSeries = infoUsuario["pagosSeries"].toString(),
            DefaultPriceList = infoUsuario["priceList"].toString(),
            DefaultWarehouse = infoUsuario["whsCode"].toString(),
            DefaultCurrency = infoUsuario["currCode"].toString(),
            DefaultTaxCode = infoUsuario["taxCode"].toString(),
            DefaultProyecto = "",

            DefaultAcctCodeCh = infoUsuario["acctCheque"].toString(),
            DefaultAcctCodeDe = infoUsuario["acctDeposito"].toString(),
            DefaultAcctCodeEf = infoUsuario["acctEfectivo"].toString(),
            DefaultAcctCodeTr = infoUsuario["acctTransferencia"].toString(),

            DefaultConductor =  "CON001",
            DefaultZona = infoUsuario["zonaUsuario"].toString(),

            AccStatusSession = binding.cbUsuarioSesionActiva.isChecked.getStringBool(),
            AccLocked = binding.cbUsuarioActivo.isChecked.getStringForAccLocked(),
            SuperUser = binding.cbUsuarioSuperUsuario.isChecked.getStringBool(),
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
        /*if (binding.txvUsuarioGeneralCorreoValue.text.toString().isEmpty())emptyFields.add("\n Correo")*/
        /*if (binding.txvUsuarioGeneralTelefonoValue.text.toString().isEmpty())emptyFields.add("\n Telefono")*/
        if (binding.txvUsuarioGeneralVendedorValue.text.toString().isEmpty())emptyFields.add("\n Vendedor")
        /*if (binding.txvUsuarioGeneralSerieClientesValue.text.toString().isEmpty())emptyFields.add("\n SerieClientes")
        if (binding.txvUsuarioGeneralSeriePedidosValue.text.toString().isEmpty())emptyFields.add("\n SeriePedidos")
        if (binding.txvUsuarioGeneralSeriePagosValue.text.toString().isEmpty())emptyFields.add("\n SeriePagos")*/
        if (binding.txvUsuarioGeneralListaPrecioValue.text.toString().isEmpty())emptyFields.add("\n ListaPrecio")
        if (binding.txvUsuarioGeneralImpuestoValue.text.toString().isEmpty())emptyFields.add("\n Impuesto")
        if (binding.txvUsuarioGeneralMonedaValue.text.toString().isEmpty())emptyFields.add("\n Moneda")
        if (binding.txvUsuarioGeneralAlmacenValue.text.toString().isEmpty())emptyFields.add("\n Almacen")
       /*if (binding.txvUsuarioGeneralCuentaEfectivoValue.text.toString().isEmpty())emptyFields.add("\n CuentaEfectivo")
        if (binding.txvUsuarioGeneralCuentaTransferenciaValue.text.toString().isEmpty())emptyFields.add("\n CuentaTransferencia")
        if (binding.txvUsuarioGeneralCuentaDepositoValue.text.toString().isEmpty())emptyFields.add("\n CuentaDeposito")
        if (binding.txvUsuarioGeneralCuentaChequeValue.text.toString().isEmpty())emptyFields.add("\n CuentaCheque")*/
        return emptyFields.joinToString()
    }

    private fun validateUserInfoExists(usuarioCabecera: DoConfigurarUsuario){
        usuariosViewModel.validarCreacionUsuario(usuarioCabecera, accion)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_aceptar_check, menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_aceptar -> {
                try {
                    /*val listValues = listOf(
                        infoUsuario["acctCheque"].toString(),
                        infoUsuario["acctDeposito"].toString(),
                        infoUsuario["acctEfectivo"].toString(),
                        infoUsuario["acctTransferencia"].toString(),

                        infoUsuario["pedidoSeries"].toString(),
                        infoUsuario["socioSeries"].toString(),
                        infoUsuario["pagosSeries"].toString(),
                        infoUsuario["priceList"].toString(),
                        infoUsuario["whsCode"].toString(),
                        infoUsuario["currCode"].toString(),
                        infoUsuario["taxCode"].toString()
                    )
                    val usuarioCabecera = getUsuarioCabecera()
                    *//*Toast.makeText(this, listValues.joinToString(separator =" \n"), Toast.LENGTH_SHORT).show()*//*
                    Toast.makeText(this,"${usuarioCabecera.AccStatusSession}- ${usuarioCabecera.AccLocked}", Toast.LENGTH_SHORT).show()*/

                    val fieldEmpty = validateEmptyFields()
                    if(fieldEmpty.isNotEmpty()){
                        throw Exception("Debe completar el campo $fieldEmpty")
                    }

                    validateUserInfoExists(getUsuarioCabecera())

                } catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


}