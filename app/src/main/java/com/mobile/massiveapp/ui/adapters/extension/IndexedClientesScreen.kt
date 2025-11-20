package com.mobile.massiveapp.ui.adapters.extension

import com.mobile.massiveapp.domain.model.DoClienteScreen

class IndexedClientesScreen(val clientes: List<DoClienteScreen>) {
    private val cardNameIndex: Map<String, List<DoClienteScreen>> = clientes.groupBy { it.CardName.lowercase() }
    private val licTradNumIndex: Map<String, List<DoClienteScreen>> = clientes.groupBy { it.LicTradNum.trim() }

    // Agrega más índices para otros campos si es necesario...

    fun buscarPorCardName(cardName: String): List<DoClienteScreen> {
        return cardNameIndex.filterKeys { it.contains(cardName) }.values.flatten()
    }

    fun buscarPorLicTradNum(licTradNum: String): List<DoClienteScreen> {
        return licTradNumIndex[licTradNum] ?: emptyList()
    }

    fun buscarPorCoincidenciaParcial(cardName: String): List<DoClienteScreen> {
        val normalizedCardName = cardName.lowercase().trim()
        val resultados = mutableListOf<DoClienteScreen>()

        // Busca coincidencias parciales en el campo CardName
        for ((key, value) in cardNameIndex) {
            if (key.contains(normalizedCardName)) {
                resultados.addAll(value)
            }
        }

        // Busca coincidencias parciales en el campo LicTradNum
        for ((key, value) in licTradNumIndex) {
            if (key.contains(normalizedCardName)) {
                resultados.addAll(value)
            }
        }

        return resultados.distinct() // Elimina duplicados
    }
}