package com.mobile.massiveapp.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.data.util.getSerialID
import com.mobile.massiveapp.databinding.FragmentAuthBinding
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.ui.base.BaseDialogSImpleLoading
import com.mobile.massiveapp.ui.view.configuracion.ConfiguracionActivity
import com.mobile.massiveapp.ui.view.menu.MenuActivity
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import com.mobile.massiveapp.ui.viewmodel.DatosMaestrosViewModel
import com.mobile.massiveapp.ui.viewmodel.GeneralViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val configuracionViewModel: ConfiguracionViewModel by viewModels()
    private val generalViewModel: GeneralViewModel by viewModels()
    private val datosMaestrosViewModel: DatosMaestrosViewModel by viewModels()

    private val IP_LOCAL = "192.168.1.3"
    //http://45.71.35.225:82/WebS_APPMOVIL.asmx
    //private val IP_PUBLICA = "45.232.150.245"http://45.71.35.225:82/WebS_APPMOVIL.asmx

    private val IP_PUBLICA = "45.71.35.225"
    private val PUERTO = "82"
    private val BASE_DEFAULT = "MSV_MOVIL_PRAGSA"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

            //Btn Config
        binding.btnConfiguracion.setOnClickListener {
            Intent(requireContext(), ConfiguracionActivity::class.java).apply {
                startActivity(this)
            }
        }

        if (requireActivity().intent.getBooleanExtra("splash", false)){
            setConfiguracionDefault()
        }

            //Para no mostrar sugerencias en el EDT Usuario
        binding.edtTextEmail.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            //Login
        binding.btnLogin.setOnClickListener {
            usuarioViewModel.login(
                version = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName,
                usuario = binding.edtTextEmail.text.toString(),
                password = binding.edtTextPassword.text.toString(),
                context = requireContext()
            )
        }

            //Control del progressbar
        usuarioViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressbar.isVisible = it
        }

        return binding.root
    }

    private fun setConfiguracionDefault() {
        val isLoadingDialog = BaseDialogSImpleLoading(requireActivity())
        generalViewModel.isLoadingBases.observe(viewLifecycleOwner){
            if (it){
                isLoadingDialog.startLoading()
            } else {
                isLoadingDialog.onDismiss()
            }
        }
        generalViewModel.getAllBasesDisponibles(
            IP_PUBLICA,
            PUERTO
        )
        generalViewModel.dataGetAllBasesDisponibles.observe(viewLifecycleOwner){ bases->
            val baseDefault = if (bases.isNotEmpty()){
                bases.filter { bses -> bses.Code== "0001" }.firstOrNull()?.DataBase?:BASE_DEFAULT
            } else {
                BASE_DEFAULT
            }

            configuracionViewModel.saveConfiguracion(
                DoConfiguracion(
                    IpPublica = IP_PUBLICA,
                    IpLocal= IP_LOCAL,
                    NumeroPuerto = PUERTO,
                    BaseDeDatos = baseDefault,
                    SincAutomatica = false,
                    IMEI = getSerialID(requireContext()),
                    TimerServicio = 15,
                    TopArticulo = 0,
                    TopFactura = 0,
                    TopCliente = 0,
                    true,
                    false,
                    false,
                    25,
                    prefsApp.getVersionApp()
                )
            )
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usuarioViewModel.loginSuccess.observe(viewLifecycleOwner) { response->

            if (response.ErrorMensaje == "Login exitoso") {

                Toast.makeText(requireContext(), response.ErrorMensaje, Toast.LENGTH_SHORT).show()

                Intent(requireContext(), MenuActivity::class.java)
                    .putExtra("login", true)
                    .apply { startActivity(this) }

            } else {
                Toast.makeText(requireContext(), response.ErrorMensaje, Toast.LENGTH_SHORT).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun configuracionDontExist() =
        try {
            var config = DoConfiguracion()
            configuracionViewModel.getConfiguracionActual()
            configuracionViewModel.dataGetConfiguracionActual.observe(viewLifecycleOwner){ config = it }
            !(config.IpPublica != "" && config.NumeroPuerto != "")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


}