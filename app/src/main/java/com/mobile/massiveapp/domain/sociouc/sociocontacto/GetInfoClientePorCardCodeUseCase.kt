package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.GeneralZonasDao
import com.mobile.massiveapp.domain.model.DoClienteInfo
import javax.inject.Inject

class GetInfoClientePorCardCodeUseCase @Inject constructor(
    private val sociosDao: ClienteSociosDao,
    private val zonasDao: GeneralZonasDao
){
    suspend operator fun invoke(cardCode: String) =
        try {
            val codeFormated = getCodeZonaFormated(sociosDao.getClienteSocioPorCardCode(cardCode).Zone)

            val response = sociosDao.getInfoCliente(cardCode, codeFormated)
            val finalResponse = DoClienteInfo(
                CardName = response.CardName,
                CardCode = response.CardCode,
                CardType = if(response.CardType == "C") "Cliente" else "--",
                U_MSV_LO_TIPOPER = if(response.U_MSV_LO_TIPOPER =="TPN") "Natural" else if(response.U_MSV_LO_TIPOPER =="TPJ") "Jurídica" else "--",
                U_MSV_LO_TIPODOC = if(response.U_MSV_LO_TIPODOC == "1") "REGISTRO NACIONAL DE INDENTIDAD" else if(response.U_MSV_LO_TIPODOC == "6")"REGISTRO UNICO DE CONTRIBUYENTES" else "--",
                LicTradNum = response.LicTradNum,
                GroupName = response.GroupName,
                Currency = response.Currency,
                U_MSV_LO_APELPAT = response.U_MSV_LO_APELPAT,
                U_MSV_LO_APELMAT = response.U_MSV_LO_APELMAT,
                U_MSV_LO_PRIMNOM = response.U_MSV_LO_PRIMNOM,
                U_MSV_LO_SEGUNOM = response.U_MSV_LO_SEGUNOM,
                E_Mail = response.E_Mail,
                Phone1 = response.Phone1,
                Phone2 = response.Phone2,
                Cellular = response.Cellular,
                PymntGroup = response.PymntGroup,
                ListName = response.ListName,
                Indicador = response.Indicador,
                Zona = response.Zona
            )

     /*       response.CardType = if(response.CardType == "C") "Cliente" else "--"
            response.U_MSV_LO_TIPOPER = if(response.U_MSV_LO_TIPOPER =="TPN") "Natural" else if(response.U_MSV_LO_TIPOPER =="TPJ") "Jurídica" else "--"
            response.U_MSV_LO_TIPODOC = if(response.U_MSV_LO_TIPODOC == "1") "REGISTRO NACIONAL DE INDENTIDAD" else if(response.U_MSV_LO_TIPODOC == "6")"REGISTRO UNICO DE CONTRIBUYENTES" else "--"*/
            finalResponse
        } catch (e: Exception){
            e.printStackTrace()
            DoClienteInfo()
        }

    private fun getCodeZonaFormated(code: String): String {
        var codeFormated = code
        if (code.trim().isNotEmpty()){
            val codeInt = code.toIntOrNull()?:0
            if (codeInt in 1..9){
                codeFormated = "0$codeInt"
            }
        }
        return codeFormated
    }
}