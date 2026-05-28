package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import com.mobile.massiveapp.domain.model.DoDireccion
import javax.inject.Inject

class UpdateAddressRutaComercialDetalleUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao,
    private val socioDireccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(accDocEntry: String, address: DoDireccion, lineNum: Int) =
        try {
            rutaDetalleDao.updateAddress(accDocEntry, address.Street, address.AdresType, lineNum)
            val rutaDetalle = rutaDetalleDao.getDetalle(accDocEntry = accDocEntry, docLine = lineNum)
            val addressSocio = socioDireccionesDao.getDireccionPorTipoYStreet(address.Street, address.AdresType)

            rutaDetalleDao.insertAll(
                listOf(
                    RutaComercialDetalleEntity(
                        AccDocEntry = rutaDetalle.AccDocEntry,
                        LineNum = rutaDetalle.LineNum,
                        AccAction = rutaDetalle.AccAction,
                        AccCreateDate = rutaDetalle.AccCreateDate,
                        AccCreateHour = rutaDetalle.AccCreateHour,
                        AccCreateUser = rutaDetalle.AccCreateUser,
                        AccUpdateDate = rutaDetalle.AccUpdateDate,
                        AccUpdateHour = rutaDetalle.AccUpdateHour,
                        AccUpdateUser = rutaDetalle.AccUpdateUser,
                        AccMigrated = rutaDetalle.AccMigrated,
                        AccControl = rutaDetalle.AccControl,
                        Status = rutaDetalle.Status,
                        CardCode = rutaDetalle.CardCode,
                        Address = addressSocio.Address,//------
                        AddressType = address.AdresType,//-----
                        Comments = rutaDetalle.Comments,
                        ObjType = rutaDetalle.ObjType,
                        DocEntry = rutaDetalle.DocEntry,
                        U_MSV_CP_LATITUD = rutaDetalle.U_MSV_CP_LATITUD,
                        U_MSV_CP_LONGITUD = rutaDetalle.U_MSV_CP_LONGITUD,
                        Country = addressSocio.Country,//
                        State = addressSocio.State,//
                        County = addressSocio.County,//
                        City = addressSocio.City,//
                        ZipCode = addressSocio.ZipCode,//
                        Street = addressSocio.Street,//
                        Block = addressSocio.Block,//
                        U_MSV_FE_UBI = addressSocio.U_MSV_FE_UBI,//
                    )
                )
            )
            true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }

}
