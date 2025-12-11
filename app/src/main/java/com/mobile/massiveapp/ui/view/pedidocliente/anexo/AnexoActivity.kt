package com.mobile.massiveapp.ui.view.pedidocliente.anexo

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.mobile.massiveapp.R
import com.mobile.massiveapp.databinding.ActivityAnexoBinding
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import com.mobile.massiveapp.ui.adapters.AnexoAdapter
import com.mobile.massiveapp.ui.view.util.SearchViewHelper
import com.mobile.massiveapp.ui.viewmodel.AnexoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AnexoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnexoBinding
    private lateinit var anexoAdapter: AnexoAdapter
    private val anexoViewModel: AnexoViewModel by viewModels()

    private lateinit var photoUri: Uri
    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Aquí recibes el URI de la imagen guardada
                val finalUri = photoUri
                // úsalo como necesites
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnexoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDetaultUi()
        setValoresIniciales()

    }

    private fun setValoresIniciales() {

    }

    private fun setDetaultUi() {
        anexoAdapter = AnexoAdapter(
            emptyList(),
            onClickListener = { imagen-> },
            onLongPressListener ={ view, imagen -> })

        binding.rvAnexoImagenes.adapter = anexoAdapter

    }

    private fun onImageSelected(uri: Uri) {
        // 1. Mostrarla en un ImageView con Glide (por ejemplo, una vista previa)
        /*Glide.with(this)
            .load(uri)
            .into(binding.imagenPreview)

        // 2. Guardar en la BD (Room) como String
        val anexo = DoAnexoImagen(
            Id = 0, // o lo que uses
            Nombre = uri.lastPathSegment ?: "imagen_${System.currentTimeMillis()}",
            Uri = uri.toString()
        )*/

    }
    private fun createInternalImageUri(): Uri {
        val imagesDir = File(filesDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()

        val imageFile = File(imagesDir, "img_${System.currentTimeMillis()}.jpg")

        return FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            imageFile
        )
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_anexo, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_upload -> {

            }

            R.id.app_bar_camara -> {
                photoUri = createInternalImageUri()
                takePhotoLauncher.launch(photoUri)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}