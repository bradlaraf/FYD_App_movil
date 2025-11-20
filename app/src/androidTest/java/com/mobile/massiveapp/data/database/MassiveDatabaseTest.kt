package com.mobile.massiveapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobile.massiveapp.data.database.dao.ArticuloDao
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class MassiveDatabaseTest: TestCase() {

    private lateinit var db: MassiveDatabase
    private lateinit var dao: ArticuloDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MassiveDatabase::class.java).build()
        dao = db.getArticuloDao()

    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun testArticuloInfo () = runBlocking {

        val itemCodeToTest = "A0000000001"
        val articuloInfo = dao.getArticuloInfoConUnidadDeMedida(itemCodeToTest)

        // Verificar que el resultado no sea nulo
        assertNotNull(articuloInfo)

        // Realizar las aserciones para comprobar el resultado de la consulta
        assertEquals("A0000000001", articuloInfo.ItemCode)
        assertEquals("Nombre del artículo", articuloInfo.ItemName)
        // Asegúrate de comparar otros valores de acuerdo a la clase DoArticuloInfo
        // Puedes seguir con las demás aserciones para cada campo en la clase DoArticuloInfo
        assertEquals(100.0, articuloInfo.OnOrder, 0.001) // Asegúrate de comparar valores con una tolerancia adecuada para números de punto flotante
        assertEquals(50, articuloInfo.OnHand)
        assertEquals("Grupo de artículos", articuloInfo.ItmsGrpNam)
        assertEquals("Nombre del fabricante", articuloInfo.FirmName)
    }
}