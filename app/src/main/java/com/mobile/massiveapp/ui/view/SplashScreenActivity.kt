package com.mobile.massiveapp.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.databinding.ActivitySplashScreenBinding
import com.mobile.massiveapp.ui.view.login.LoginActivity
import com.mobile.massiveapp.ui.view.menu.MenuActivity
import com.mobile.massiveapp.ui.view.util.clearExternalCache
import com.mobile.massiveapp.ui.view.util.clearInternalCache
import com.mobile.massiveapp.ui.view.util.performActionWithRetry
import com.mobile.massiveapp.ui.viewmodel.ConfiguracionViewModel
import com.mobile.massiveapp.ui.viewmodel.ProviderViewModel
import com.mobile.massiveapp.ui.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashScreenBinding
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val configuracionViewMode: ConfiguracionViewModel by viewModels()
    private val providerViewModel: ProviderViewModel by viewModels()

    private val required_permissions = if(Build.VERSION.SDK_INT >= 33){
        arrayListOf(
        android.Manifest.permission.READ_MEDIA_AUDIO,
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.READ_MEDIA_VIDEO,
        android.Manifest.permission.POST_NOTIFICATIONS,
            //Permisos de ubicación
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    } else {
        arrayListOf(
            //Permisos de ubicación
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,

            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txvVersionApp.text = packageManager.getPackageInfo(packageName, 0).versionName

        clearCache()
        checkPermissions()
        saveVersionApp()

        providerViewModel.savePermiso(false)
        providerViewModel.permiso.observe(this){
            screenSplash.setKeepOnScreenCondition{ true }
        }



        usuarioViewModel.dataGetUsuarioFromDatabase.observe(this){ usuario->
            try {
                if (usuario.Code.isEmpty()){
                    Intent(this, LoginActivity::class.java)
                        .putExtra("splash", true)
                        .also { startActivity(it) }
                    finish()
                } else {
                    prefsApp.saveUserName(usuario.Name)
                    Intent(this, MenuActivity::class.java).also {
                        startActivity(it)
                    }
                    finish()
                }
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Timber.d("Error: ${e.message}")
            }
        }






    }

    private fun clearCache() {
        try {
            clearInternalCache(this)
            clearExternalCache(this)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun saveVersionApp() {
        try {
            val version = performActionWithRetry({
                getVersionApp()
            }, maxRetries = 3)


            prefsApp.saveVersionApp(version?:"")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getVersionApp():String {
        return packageManager.getPackageInfo(packageName, 0).versionName
    }

    private fun checkPermissions() {
        try {
            val permissionsToRequest = mutableListOf<String>()

            // Comprobar cada permiso en la lista y agregar los que no se tienen a la lista de permisos a solicitar

            for (permission in required_permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission)
                }
            }

            if (permissionsToRequest.isNotEmpty()) {
                // Convertir la lista de permisos a un array para solicitarlos
                val permissionsArray = permissionsToRequest.toTypedArray()
                // Solicitar permisos al usuario
                ActivityCompat.requestPermissions(this, permissionsArray, 233)
            } else {
                usuarioViewModel.getUsuarioFromDatabase()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al solicitar permisos", Toast.LENGTH_SHORT).show()
            Timber.e(e)
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            233 -> {
                try {
                    // Verificar si se concedieron todos los permisos solicitados
                    if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                        providerViewModel.savePermiso(true)

                        usuarioViewModel.getUsuarioFromDatabase()
                    } else {
                        Toast.makeText(this, "No se concedieron todos los permisos", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al solicitar permisos", Toast.LENGTH_SHORT).show()
                    Timber.e(e)
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
