package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.domain.model.DoFormaPagoItemView
import javax.inject.Inject

class GetFormaPagoItemViewUseCase @Inject constructor(

) {
    suspend operator fun invoke()=
        try {
            listOf(
                DoFormaPagoItemView(
                    Id = 0,
                    Title = "Tipo de pago",
                    Value = "",
                    Arrow = "Y",
                ),
                DoFormaPagoItemView(
                    Id = 1,
                    Title = "Cuenta",
                    Value = "",
                    Arrow = "N",
                ),
                DoFormaPagoItemView(
                    Id = 2,
                    Title = "Referencia",
                    Value = "",
                    Arrow = "Y",
                ),
                DoFormaPagoItemView(
                    Id = 3,
                    Title = "Importe",
                    Value = "",
                    Arrow = "N",
                ),

            )
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}