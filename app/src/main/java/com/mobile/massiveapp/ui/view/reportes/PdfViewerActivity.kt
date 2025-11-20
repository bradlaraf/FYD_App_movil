package com.mobile.massiveapp.ui.view.reportes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.mobile.massiveapp.databinding.ActivityPdfViewerBinding
import timber.log.Timber

class PdfViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val path = intent.getStringExtra("path")
        val fileName = intent.getStringExtra("fileName")

        val uris =
            try {
                Uri.parse(path)
            } catch (e: Exception) {
                null
            }




        binding.btnEscogerPdf.setOnClickListener {

            launchPDF.launch("application/pdf")

            /*val file = path?.let {
                Timber.d("path: $it")
                File(it)
            }

            val uri =
                try {
                    file?.let {
                        FileProvider.getUriForFile(this, applicationContext.packageName + ".provider",
                            it
                        )
                    }
                } catch (e: Exception) {
                    null
                }*/


            /*if (file != null){

                binding.pdfView.fromUri(uri)
                    .spacing(12)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .enableDoubletap(true)
                    .load()
                binding.pdfView.fitToWidth()
                binding.pdfView.useBestQuality(true)
            }*/

        }


    }




    private val launchPDF = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){ uri->

        uri?.let {
            Timber.d("uri: ${it.path}")
            /*binding.pdfView.fromUri(it)
                .spacing(12)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .enableDoubletap(true)
                .load()
            binding.pdfView.fitToWidth()
            binding.pdfView.useBestQuality(true)*/
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}