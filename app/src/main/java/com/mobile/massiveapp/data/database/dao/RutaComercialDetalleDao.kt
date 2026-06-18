package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaComercialDetalleDao : BaseDao<RutaComercialDetalleEntity> {

    @Query("""
        SELECT
            T0.AccDocEntry,
            T0.LineNum,
            T0.CardCode,
            T0.Address,
            IFNULL((SELECT Z0.CardName FROM SocioNegocio Z0 WHERE Z0.CardCode = T0.CardCode), '') AS CardName,
            IFNULL(T0.Street, '') AS Street,
            IFNULL((SELECT Z0.AccMigrated FROM RutaComercial Z0 WHERE Z0.AccDocEntry = T0.AccDocEntry LIMIT 1), 'N') AS AccMigrated,
            T0.Status,
            T0.AddressType,
            T0.Comments,
            T0.ObjType,
            T0.DocEntry,
            T0.U_MSV_CP_LATITUD AS Latitud, 
            T0.U_MSV_CP_LONGITUD AS Longitud,
            T0.AccUpdateDate AS AccCreateDate,
            T0.AccUpdateHour AS AccCreateHour
        FROM RutaComercialDetalle T0
        WHERE T0.AccDocEntry = :accDocEntry
        ORDER BY T0.LineNum ASC
    """)
    fun getAllDetalleViewFlow(accDocEntry: String): Flow<List<DoRutaComercialDetalleView>>

    @Query("""
        DELETE FROM RutaComercialDetalle
        WHERE AccDocEntry = :accDocEntry
        AND LineNum = :docLine
    """)
    suspend fun deleteRutaDetalle(docLine: Int, accDocEntry: String)

    @Query("SELECT * FROM RutaComercialDetalle WHERE AccDocEntry = :accDocEntry ORDER BY LineNum ASC")
    fun getAllByAccDocEntryFlow(accDocEntry: String): Flow<List<RutaComercialDetalleEntity>>

    @Query("SELECT * FROM RutaComercialDetalle WHERE AccDocEntry = :accDocEntry ORDER BY LineNum ASC")
    suspend fun getAllByAccDocEntry(accDocEntry: String): List<RutaComercialDetalleEntity>

    @Query("SELECT COUNT(*) FROM RutaComercialDetalle WHERE AccDocEntry = :accDocEntry")
    suspend fun countByAccDocEntry(accDocEntry: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<RutaComercialDetalleEntity>)

    @Query("""
        SELECT 
        * 
        FROM RutaComercialDetalle T0
        WHERE T0.AccDocEntry = :accDocEntry
    """)
    suspend fun getAllByCode(accDocEntry: String):List<RutaComercialDetalleEntity>

    @Query("""
        SELECT
        * 
        FROM RutaComercialDetalle T0
        WHERE T0.AccDocEntry = :accDocEntry
            AND T0.LineNum = :docLine
    """)
    suspend fun getDetalle(accDocEntry: String, docLine: Int):RutaComercialDetalleEntity

    @Query("""
        DELETE FROM RutaComercialDetalle 
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun clearByAccDocEntry(accDocEntry: String)

    @Query("""
        UPDATE RutaComercialDetalle 
        SET Address = :address,
            AddressType = :addressType
        WHERE AccDocEntry = :accDocEntry AND LineNum = :lineNum
    """)
    suspend fun updateAddress(accDocEntry: String, address: String, addressType: String, lineNum: Int)


    @Query("""
        UPDATE RutaComercialDetalle
        SET 
            AccAction = 'U',
            AccMigrated = 'N',
            AccControl = 'N',
            AccUpdateHour = :horaUpdate,
            AccUpdateUser = :userUpdate,
            AccUpdateDate = :fechaUpdate,
            LineNum = :docLineNuevo
        WHERE AccDocEntry = :accDocEntry 
            AND LineNum = :docLineActual
    """)
    suspend fun updateDetalle(horaUpdate: String, fechaUpdate: String, userUpdate: String, accDocEntry: String, docLineActual: Int, docLineNuevo: Int)

    @Query("""
        UPDATE RutaComercialDetalle
        SET
            Status = 'A',
            Comments = :comentario,
            U_MSV_CP_LATITUD = :latitud,
            U_MSV_CP_LONGITUD = :longitud,
            AccUpdateHour = :horaUpdate,
            AccUpdateDate = :fechaUpdate
        WHERE AccDocEntry = :accDocEntry
            AND LineNum = :lineNum
    """)
    suspend fun confirmarDetalle(comentario: String, accDocEntry: String, latitud: String, longitud: String,lineNum: Int, horaUpdate: String, fechaUpdate: String)

    @Query("""
        UPDATE RutaComercialDetalle     
            SET Status = 'R' 
        WHERE AccDocEntry = :accDocEntry 
            AND LineNum = :docLine
    """)
    suspend fun cancelarRutaDetalle(docLine: Int, accDocEntry: String)

    @Query("""
      UPDATE RutaComercialDetalle
      SET
          AccAction = 'U',
          AccMigrated = 'N',
          AccControl = 'N',
          AccUpdateHour = :horaUpdate,
          AccUpdateUser = :userUpdate,
          AccUpdateDate = :fechaUpdate,
          LineNum = :docLineNuevo
      WHERE AccDocEntry = :accDocEntry
          AND LineNum = :docLineActual
  """)
    suspend fun updateDocLine(
        docLineNuevo: Int,
        docLineActual: Int,
        accDocEntry: String,
        horaUpdate: String,
        fechaUpdate: String,
        userUpdate: String
    )

    @Transaction
    suspend fun reorderDocLines(
        currentDocLines: List<Int>,
        accDocEntry: String,
        horaUpdate: String,
        fechaUpdate: String,
        userUpdate: String
    ) {
        currentDocLines.forEachIndexed { index, docLineActual ->
            updateDocLine(-(index + 1), docLineActual, accDocEntry, horaUpdate, fechaUpdate, userUpdate)
        }
        currentDocLines.forEachIndexed { index, _ ->
            updateDocLine(index, -(index + 1), accDocEntry, horaUpdate, fechaUpdate, userUpdate)
        }
    }

    @Query("DELETE FROM RutaComercialDetalle WHERE AccDocEntry = :accDocEntry AND CardCode = :cardCode")
    suspend fun deleteByCardCode(accDocEntry: String, cardCode: String)

    @Query("DELETE FROM RutaComercialDetalle")
    suspend fun clearAll()
}
