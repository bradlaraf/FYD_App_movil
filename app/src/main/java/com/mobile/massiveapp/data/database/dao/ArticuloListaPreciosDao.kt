package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloListaPreciosEntity
import com.mobile.massiveapp.domain.model.DoArticuloPrecioYNombreLista
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

@Dao
interface ArticuloListaPreciosDao: BaseDao<ArticuloListaPreciosEntity> {

    @Query("SELECT * FROM ListaPrecio")
    suspend fun getAllArticuloListaPrecios(): List<ArticuloListaPreciosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloListaPrecios(articuloListaPreciosEntity: List<ArticuloListaPreciosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloListaPreciosEntity: ArticuloListaPreciosEntity)


    @Update
    suspend fun updateArticuloListaPrecios(articuloListaPreciosEntity: List<ArticuloListaPreciosEntity>)

    @Query("""
            SELECT
                T0.ListNum as Code,
                T0.ListName as Name,
                0 as Checked
            FROM ListaPrecio T0
            """)
    suspend fun getListasPrecioCreacion(): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM ListaPrecio")
    suspend fun deleteAllArticuloListaPrecios()

    //Querys no Genericas
    @Query("SELECT P.Price, L.ListName FROM ArticuloPrecio P INNER JOIN ListaPrecio L ON P.PriceList = L.ListNum")
    suspend fun getArticuloPrecioYNombreLista(): List<DoArticuloPrecioYNombreLista>

}
