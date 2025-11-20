package com.mobile.massiveapp.domain.reportes

import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.ui.view.reportes.pdf.PdfGenerator
import java.lang.Exception
import javax.inject.Inject

class GenerateReportePedidosUseCase @Inject constructor(
    private val pedidosRepository: PedidoRepository
) {
    suspend operator fun invoke():Boolean =
        try {
            var success = false
            val listaTodosClientes = pedidosRepository.getAllPedidosCliente()
            val pdfGenerator = PdfGenerator(listaTodosClientes){
                success = it
            }
            pdfGenerator()
            success
        } catch (e: Exception){
            false
        }


}