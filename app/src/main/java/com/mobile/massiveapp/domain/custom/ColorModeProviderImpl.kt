package com.mobile.massiveapp.domain.custom

import com.mobile.massiveapp.R
import com.mobile.massiveapp.ui.view.configuracion.ColorModeProvider

class ColorModeProviderImpl: ColorModeProvider {
    override fun getColoredTheme(): Int {
        // Aquí proporciona la lógica para obtener el tema coloreado deseado
        // Puedes devolver el ID de estilo del tema coloreado
        return R.style.Base_Theme_Test
    }

    override fun switchColor() {
        // Aquí proporciona la lógica para cambiar el color del tema si es necesario
    }
}