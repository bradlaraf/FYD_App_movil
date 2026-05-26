package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
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
            IFNULL(T1.CardName, '') AS CardName,
            CASE WHEN T0.Address != '' THEN T0.Address
                 ELSE IFNULL((SELECT T2.Street FROM SocioDirecciones T2 WHERE T2.CardCode = T0.CardCode AND T2.AdresType = 'B' LIMIT 1), '')
            END AS Street,
            T0.AccMigrated,
            T0.Status,
            T0.AddressType,
            T0.Comments,
            T0.ObjType,
            T0.DocEntry
        FROM RutaComercialDetalle T0
        LEFT JOIN SocioNegocio T1 ON T0.CardCode = T1.CardCode
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
        WHERE AccDocEntry = :accDocEntry AND CardCode = :cardCode
    """)
    suspend fun updateAddress(accDocEntry: String, cardCode: String, address: String, addressType: String)

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
            Comments = :comentario,
            Status = ''
        WHERE AccDocEntry = :accDocEntry
            AND LineNum = :lineNum
    """)
    suspend fun confirmarDetalle(comentario: String, accDocEntry: String, lineNum: Int)

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
