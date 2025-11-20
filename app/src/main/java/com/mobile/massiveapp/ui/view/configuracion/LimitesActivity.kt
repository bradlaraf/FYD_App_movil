package com.mobile.massiveapp.ui.view.configuracion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityLimitesBinding
import com.mobile.massiveapp.ui.base.BaseDialogEdtWithTypeEdt
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LimitesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLimitesBinding
    private val configuracionViewModel: ConfiguracionViewModel by viewModels()
    private val defaultTop = "0"
    private val defaultClientes = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLimitesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDefaultUi()


        configuracionViewModel.getConfiguracionActual()
        configuracionViewModel.dataGetConfiguracionActual.observe(this){ config->
            if (config.IpPublica.isNotEmpty()){
                val usarLimites = config.UsarLimites
                binding.cbConfiguracionActivarLimite.isChecked = usarLimites
                binding.txvConfiguracionLimiteArticulosValue.text = config.TopArticulo.toString()
                binding.txvConfiguracionLimiteClientesValue.text = config.TopCliente.toString()
                binding.txvConfiguracionLimiteFacturasValue.text = config.TopFactura.toString()

                setCamposVisibles(usarLimites)

            } else {
                binding.txvConfiguracionActivarLimiteValue.text = "Desactivado"
                setCamposVisibles(false)
            }
        }


    }



    fun setCamposVisibles(usarLimites: Boolean){

        if (usarLimites){
            binding.txvConfiguracionActivarLimiteValue.text = "Activado"
        } else {
            binding.txvConfiguracionActivarLimiteValue.text = "Desactivado"
        }

        binding.clConfiguracionLimiteArticulos.isVisible = usarLimites
        binding.clConfiguracionLimiteClientes.isVisible = usarLimites
        binding.clConfiguracionLimiteFacturas.isVisible = usarLimites
    }



    fun setDefaultUi(){

        binding.txvConfiguracionLimiteArticulosValue.text = defaultTop
        binding.txvConfiguracionLimiteClientesValue.text = defaultClientes
        binding.txvConfiguracionLimiteFacturasValue.text = defaultTop


            //Limites
        binding.clConfiguracionActivarLimite.setOnClickListener {
            val usarLimites = binding.cbConfiguracionActivarLimite.isChecked
            binding.cbConfiguracionActivarLimite.isChecked = !usarLimites
            setCamposVisibles(!usarLimites)
        }

        binding.cbConfiguracionActivarLimite.setOnClickListener {
            setCamposVisibles(binding.cbConfiguracionActivarLimite.isChecked)
        }

            //Clientes
        binding.clConfiguracionLimiteClientes.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.txvConfiguracionLimiteClientesValue.text.toString()
            ){referencia->
                binding.txvConfiguracionLimiteClientesValue.text = referencia
            }.show(supportFragmentManager, "DepositoDialog")
        }

            //Articulos
        binding.clConfiguracionLimiteArticulos.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.txvConfiguracionLimiteArticulosValue.text.toString()
            ){referencia->
                binding.txvConfiguracionLimiteArticulosValue.text = referencia
            }.show(supportFragmentManager, "DepositoDialog")
        }

            //Facturas
        binding.clConfiguracionLimiteFacturas.setOnClickListener {
            BaseDialogEdtWithTypeEdt(
                "phone",
                binding.txvConfiguracionLimiteFacturasValue.text.toString()
            ){referencia->
                binding.txvConfiguracionLimiteFacturasValue.text = referencia
            }.show(supportFragmentManager, "DepositoDialog")
        }
    }









    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check_delete, menu)
        menu?.findItem(R.id.app_bar_delete)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_check -> {
                try {

                    val usarLimite = binding.cbConfiguracionActivarLimite.isChecked
                    val articulo = binding.txvConfiguracionLimiteArticulosValue.text.toString().toInt()
                    val cliente = binding.txvConfiguracionLimiteClientesValue.text.toString().toInt()
                    val factura = binding.txvConfiguracionLimiteFacturasValue.text.toString().toInt()

                    configuracionViewModel.saveLimitesConfiguration(
                        usarLimite = usarLimite,
                        articulo = articulo,
                        cliente = cliente,
                        factura = factura
                    )

                    setResult(RESULT_OK, Intent()
                        .putExtra("usarLimites", usarLimite)
                        .putExtra("articulo", articulo)
                        .putExtra("clientes", cliente)
                        .putExtra("facturas", factura))
                    onBackPressedDispatcher.onBackPressed()

                } catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}


