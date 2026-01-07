package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.common.primitives.Ints
import com.mobile.massiveapp.data.database.entities.ClientePedidosEntity
import com.mobile.massiveapp.data.model.PrecioFinalView
import com.mobile.massiveapp.domain.model.ArticuloPedido
import com.mobile.massiveapp.domain.model.DoPedidoInfoView

@Dao
interface ClientePedidosDao:BaseDao<ClientePedidosEntity> {

    @Query("""
        SELECT 
            * 
        FROM ClientePedidos 
        ORDER BY CardName
    """)
    suspend fun getAll(): List<ClientePedidosEntity>

    @Query("""
        SELECT 
            * 
        FROM ClientePedidos
        WHERE DocDate = :fechaActual
        ORDER BY CardName
    """)
    suspend fun getAll(fechaActual: String): List<ClientePedidosEntity>

    @Query("""
    SELECT
        IFNULL(AP.Price, 0.0) AS precioUnitario,
        IFNULL(ROUND(
            IFNULL(AP.Price, 0) * (100 - IFNULL(GDD.Discount, 0)) / 100.0
        , 2), 0.0) AS precioFinal,
    
        IFNULL(ROUND(
            IFNULL(AP.Price, 0)
            - (IFNULL(AP.Price, 0) * (100 - IFNULL(GDD.Discount, 0)) / 100.0)
        , 2), 0.0) AS precioDescontado,
    
        IFNULL(ROUND(
            IFNULL(GDD.Discount, 0)
        , 2), 0.0) AS porcentajeDescuento,
    
        IFNULL(ROUND(
            (IFNULL(AP.Price, 0) * (100 - IFNULL(GDD.Discount, 0)) / 100.0) * 1.18
        , 2), 0.0) AS precioBruto
    FROM SocioNegocio SN
    LEFT JOIN GrupoDescuento GD ON GD.ObjCode = SN.GroupCode
    LEFT JOIN GrupoDescuentoDetalle GDD ON GDD.AbsEntry = GD.AbsEntry
    LEFT JOIN ArticuloPrecio AP ON AP.ItemCode = GDD.ObjKey
    WHERE SN.CardCode = :cardCode
      AND AP.ItemCode = :articulo
      AND AP.PriceList = 1
    """)
    suspend fun obtenerPrecioFinal(
        articulo: String,
        cardCode: String
    ): PrecioFinalView?

    /*@Query("""
SELECT
    0.0 AS PrecioIGV,
    (SELECT Z0.Price FROM ArticuloPrecio Z0 WHERE Z0.ItemCode = :articulo AND Z0.PriceList = :listaPrecio LIMIT 1) AS PrecioUnitario,
    precioFinal AS Precio,
    CASE
        WHEN precioLP > 0 AND precioFinal < precioLP
            THEN ROUND(((precioLP - precioFinal) / precioLP) * 100.0, 0)
        ELSE 0.0
    END AS PorcentajeDescuento
FROM (
    SELECT
        ROUND(
            CASE
                -- 1) PRECIO ESPECIAL POR SOCIO DE NEGOCIO
                WHEN precioSN > 0 THEN precioSN

                -- 2) PRECIO POR GRUPO DE DESCUENTO (ya calculado como precioGD)
                WHEN precioGD > 0 THEN precioGD

                -- 3) PRECIO ESPECIAL POR CANTIDAD
                WHEN precioPC > 0 THEN precioPC

                -- 4) PRECIO ESPECIAL POR PERIODO
                WHEN precioPP > 0 THEN precioPP

                -- 5) LISTA DE PRECIO
                ELSE precioLP
            END,
            :priceDec
        ) AS precioFinal,
        precioLP
    FROM (
        SELECT
            -- GRUPO SN
            IFNULL((
                SELECT S.GroupCode
                FROM SocioNegocio S
                WHERE S.CardCode = :cardCode
                LIMIT 1
            ), 0) AS grupoSN,

            -- GRUPO ARTICULO
            IFNULL((
                SELECT A.ItmsGrpCod
                FROM Articulo A
                WHERE A.ItemCode = :articulo
                LIMIT 1
            ), 0) AS grupoAr,

            -- FABRICANTE
            IFNULL((
                SELECT A.FirmCode
                FROM Articulo A
                WHERE A.ItemCode = :articulo
                LIMIT 1
            ), 0) AS fabricante,

            -- PRECIO ESPECIAL POR SOCIO DE NEGOCIO
            IFNULL((
                SELECT ROUND(T0.Price, :priceDec)
                FROM PrecioEspecial T0
                WHERE T0.ListNum  = :listaPrecio
                  AND T0.ItemCode = :articulo
                  AND T0.CardCode = :cardCode
                  AND date(:fecha) BETWEEN date(T0.ValidFrom) AND date(T0.ValidTo)
                LIMIT 1
            ), 0.0) AS precioSN,

            -- PRECIO ESPECIAL POR PERIODO Y CANTIDAD
            IFNULL((
                SELECT ROUND(T0.Price, :priceDec)
                FROM PrecioEspecial2 T0
                INNER JOIN PrecioEspecial1 T1 ON T1.Code = T0.Code
                INNER JOIN PrecioEspecial  T2 ON T2.Code = T0.Code
                WHERE date(:fecha) BETWEEN date(T2.ValidFrom) AND date(T2.ValidTo)
                  AND T1.ListNum  = :listaPrecio
                  AND T1.ItemCode = :articulo
                  AND T1.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                  AND date(:fecha) BETWEEN date(T1.FromDate) AND date(T1.ToDate)
                  AND T0.Amount <= :cantidad
                ORDER BY T0.Amount DESC
                LIMIT 1
            ), 0.0) AS precioPC,

            -- PRECIO ESPECIAL POR PERIODO
            IFNULL((
                SELECT ROUND(T0.Price, :priceDec)
                FROM PrecioEspecial1 T0
                INNER JOIN PrecioEspecial T1 ON T1.Code = T0.Code
                WHERE date(:fecha) BETWEEN date(T1.ValidFrom) AND date(T1.ValidTo)
                  AND T0.ListNum  = :listaPrecio
                  AND T0.ItemCode = :articulo
                  AND T0.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                  AND date(:fecha) BETWEEN date(T0.FromDate) AND date(T0.ToDate)
                LIMIT 1
            ), 0.0) AS precioPP,

            -- LISTA DE PRECIO BASE
            IFNULL((
                SELECT ROUND(T0.Price, :priceDec)
                FROM ArticuloPrecio T0
                WHERE T0.PriceList = :listaPrecio
                  AND T0.ItemCode  = :articulo
                LIMIT 1
            ), 0.0) AS precioLP,

            -- =========================
            -- DESCUENTOS (se elige 1 según prioridad)
            -- SN (Obj='2', ObjCode = cardCode): Artículo > Fabricante > Grupo Artículo
            IFNULL((
                SELECT D.Discount
                FROM GrupoDescuentoDetalle D
                INNER JOIN GrupoDescuento G ON G.AbsEntry = D.AbsEntry
                WHERE G.Type = 'C'
                  AND G.Obj = '2'
                  AND G.ObjCode = :cardCode
                  AND D.Obj = '4'
                  AND D.ObjKey = :articulo
                LIMIT 1
            ), 0.0) AS desc_sn_arti,

            IFNULL((
                SELECT D.Discount
                FROM GrupoDescuentoDetalle D
                INNER JOIN GrupoDescuento G ON G.AbsEntry = D.AbsEntry
                WHERE G.Type = 'C'
                  AND G.Obj = '2'
                  AND G.ObjCode = :cardCode
                  AND D.Obj = '43'
                  AND D.ObjKey = CAST((
                      SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode = :articulo LIMIT 1
                  ) AS TEXT)
                LIMIT 1
            ), 0.0) AS desc_sn_fabr,

            IFNULL((
                SELECT D.Discount
                FROM GrupoDescuentoDetalle D
                INNER JOIN GrupoDescuento G ON G.AbsEntry = D.AbsEntry
                WHERE G.Type = 'C'
                  AND G.Obj = '2'
                  AND G.ObjCode = :cardCode
                  AND D.Obj = '52'
                  AND D.ObjKey = CAST((
                      SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode = :articulo LIMIT 1
                  ) AS TEXT)
                LIMIT 1
            ), 0.0) AS desc_sn_grar,

            -- GSN (Obj='10', ObjCode = grupoSN): Artículo > Fabricante > Grupo Artículo
            IFNULL((
                SELECT D.Discount
                FROM GrupoDescuentoDetalle D
                INNER JOIN GrupoDescuento G ON G.AbsEntry = D.AbsEntry
                WHERE G.Type = 'C'
                  AND G.Obj = '10'
                  AND G.ObjCode = CAST((
                      SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode = :cardCode LIMIT 1
                  ) AS TEXT)
                  AND D.Obj = '4'
                  AND D.ObjKey = :articulo
                LIMIT 1
            ), 0.0) AS desc_gsn_arti,

            IFNULL((
                SELECT D.Discount
                FROM GrupoDescuentoDetalle D
                INNER JOIN GrupoDescuento G ON G.AbsEntry = D.AbsEntry
                WHERE G.Type = 'C'
                  AND G.Obj = '10'
                  AND G.ObjCode = CAST((
                      SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode = :cardCode LIMIT 1
                  ) AS TEXT)
                  AND D.Obj = '43'
                  AND D.ObjKey = CAST((
                      SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode = :articulo LIMIT 1
                  ) AS TEXT)
                LIMIT 1
            ), 0.0) AS desc_gsn_fabr,

            IFNULL((
                SELECT X0.Discount
                FROM GrupoDescuentoDetalle X0
                INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                WHERE X1.Type = 'C'
                  AND X1.Obj = '10'
                  AND X1.ObjCode = CAST((
                      SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode = :cardCode LIMIT 1
                  ) AS TEXT)
                  AND X0.Obj = '52'
                  AND X0.ObjKey = CAST((
                      SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode = :articulo LIMIT 1
                  ) AS TEXT)
                LIMIT 1
            ), 0.0) AS desc_gsn_grar,

            -- =========================
            -- PRECIO GD final
            ROUND(
                (IFNULL((
                    SELECT ROUND(T0.Price, :priceDec)
                    FROM ArticuloPrecio T0
                    WHERE T0.PriceList = :listaPrecio
                      AND T0.ItemCode  = :articulo
                    LIMIT 1
                ), 0.0)) *
                (100.0 - (
                    CASE
                        -- primero SN
                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='4' AND X0.ObjKey=:articulo
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='4' AND X0.ObjKey=:articulo
                            LIMIT 1
                        ),0.0)

                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='43'
                              AND X0.ObjKey = CAST((SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='43'
                              AND X0.ObjKey = CAST((SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)

                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='52'
                              AND X0.ObjKey = CAST((SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='2' AND X1.ObjCode=:cardCode
                              AND X0.Obj='52'
                              AND X0.ObjKey = CAST((SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)

                        -- si no hay SN, usa GSN
                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='4' AND X0.ObjKey=:articulo
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='4' AND X0.ObjKey=:articulo
                            LIMIT 1
                        ),0.0)

                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='43'
                              AND X0.ObjKey = CAST((SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='43'
                              AND X0.ObjKey = CAST((SELECT A.FirmCode FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)

                        WHEN (IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='52'
                              AND X0.ObjKey = CAST((SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)) > 0 THEN IFNULL((
                            SELECT X0.Discount
                            FROM GrupoDescuentoDetalle X0
                            INNER JOIN GrupoDescuento X1 ON X1.AbsEntry = X0.AbsEntry
                            WHERE X1.Type='C' AND X1.Obj='10'
                              AND X1.ObjCode = CAST((SELECT S.GroupCode FROM SocioNegocio S WHERE S.CardCode=:cardCode LIMIT 1) AS TEXT)
                              AND X0.Obj='52'
                              AND X0.ObjKey = CAST((SELECT A.ItmsGrpCod FROM Articulo A WHERE A.ItemCode=:articulo LIMIT 1) AS TEXT)
                            LIMIT 1
                        ),0.0)

                        ELSE 0.0
                    END
                )) / 100.0,
                :priceDec
            ) AS precioGD
        FROM (SELECT 1)
    )
);
""")
    suspend fun getPrecioArticulo(
        fecha: String,
        articulo: String,
        listaPrecio: Int,
        cantidad: Int,
        cardCode: String,
        priceDec: Int
    ): ArticuloPedido*/

    @Query("""
        WITH X0 AS (
            SELECT
                -- PRECIO SN
                IFNULL((
                    SELECT ROUND(T0.Price, :priceDec)
                    FROM PrecioEspecial T0
                    WHERE T0.ListNum  = :listaPrecio
                      AND T0.ItemCode = :articulo
                      AND T0.CardCode = :cardCode
                      AND date(:fecha) BETWEEN date(T0.ValidFrom) AND date(T0.ValidTo)
                    LIMIT 1
                ), 0.0) AS precioSN,
        
                -- PRECIO PC
                IFNULL((
                    SELECT ROUND(T0.Price, :priceDec)
                    FROM PrecioEspecial2 T0
                    INNER JOIN PrecioEspecial1 T1 ON T1.Code = T0.Code
                    INNER JOIN PrecioEspecial  T2 ON T2.Code = T0.Code
                    WHERE date(:fecha) BETWEEN date(T2.ValidFrom) AND date(T2.ValidTo)
                      AND T1.ListNum  = :listaPrecio
                      AND T1.ItemCode = :articulo
                      AND T1.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                      AND date(:fecha) BETWEEN date(T1.FromDate) AND date(T1.ToDate)
                      AND T0.Amount <= :cantidad
                    ORDER BY T0.Amount DESC
                    LIMIT 1
                ), 0.0) AS precioPC,
        
                -- PRECIO PP
                IFNULL((
                    SELECT ROUND(T0.Price, :priceDec)
                    FROM PrecioEspecial1 T0
                    INNER JOIN PrecioEspecial T1 ON T1.Code = T0.Code
                    WHERE date(:fecha) BETWEEN date(T1.ValidFrom) AND date(T1.ValidTo)
                      AND T0.ListNum  = :listaPrecio
                      AND T0.ItemCode = :articulo
                      AND T0.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                      AND date(:fecha) BETWEEN date(T0.FromDate) AND date(T0.ToDate)
                    LIMIT 1
                ), 0.0) AS precioPP,
        
                -- PRECIO LP
                IFNULL((
                    SELECT ROUND(T0.Price, :priceDec)
                    FROM ArticuloPrecio T0
                    WHERE T0.PriceList = :listaPrecio
                      AND T0.ItemCode  = :articulo
                    LIMIT 1
                ), 0.0) AS precioLP,
        
                -- =========================
                -- DESCUENTOS SN (Obj='2') prioridad: Artículo > Fabricante > Grupo Artículo
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='2' AND T1.ObjCode=:cardCode
                      AND T0.Obj='4' AND T0.ObjKey=:articulo
                    LIMIT 1
                ), 0.0) AS desc_sn_arti,
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='2' AND T1.ObjCode=:cardCode
                      AND T0.Obj='43'
                      AND T0.ObjKey = CAST((
                          SELECT T0.FirmCode
                          FROM Articulo T0
                          WHERE T0.ItemCode = :articulo
                          LIMIT 1
                      ) AS TEXT)
                    LIMIT 1
                ), 0.0) AS desc_sn_fabr,
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='2' AND T1.ObjCode=:cardCode
                      AND T0.Obj='52'
                      AND T0.ObjKey = CAST((
                          SELECT T0.ItmsGrpCod
                          FROM Articulo T0
                          WHERE T0.ItemCode = :articulo
                          LIMIT 1
                      ) AS TEXT)
                    LIMIT 1
                ), 0.0) AS desc_sn_grar,
        
                -- =========================
                -- DESCUENTOS GSN (Obj='10') prioridad: Artículo > Fabricante > Grupo Artículo
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='10'
                      AND T1.ObjCode = CAST((
                          SELECT T0.GroupCode
                          FROM SocioNegocio T0
                          WHERE T0.CardCode = :cardCode
                          LIMIT 1
                      ) AS TEXT)
                      AND T0.Obj='4' AND T0.ObjKey=:articulo
                    LIMIT 1
                ), 0.0) AS desc_gsn_arti,
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='10'
                      AND T1.ObjCode = CAST((
                          SELECT T0.GroupCode
                          FROM SocioNegocio T0
                          WHERE T0.CardCode = :cardCode
                          LIMIT 1
                      ) AS TEXT)
                      AND T0.Obj='43'
                      AND T0.ObjKey = CAST((
                          SELECT T0.FirmCode
                          FROM Articulo T0
                          WHERE T0.ItemCode = :articulo
                          LIMIT 1
                      ) AS TEXT)
                    LIMIT 1
                ), 0.0) AS desc_gsn_fabr,
        
                IFNULL((
                    SELECT T0.Discount
                    FROM GrupoDescuentoDetalle T0
                    INNER JOIN GrupoDescuento T1 ON T1.AbsEntry = T0.AbsEntry
                    WHERE T1.Type='C' AND T1.Obj='10'
                      AND T1.ObjCode = CAST((
                          SELECT T0.GroupCode
                          FROM SocioNegocio T0
                          WHERE T0.CardCode = :cardCode
                          LIMIT 1
                      ) AS TEXT)
                      AND T0.Obj='52'
                      AND T0.ObjKey = CAST((
                          SELECT T0.ItmsGrpCod
                          FROM Articulo T0
                          WHERE T0.ItemCode = :articulo
                          LIMIT 1
                      ) AS TEXT)
                    LIMIT 1
                ), 0.0) AS desc_gsn_grar
        ),
        X1 AS (
            SELECT
                *,
                -- descuento elegido (replica el CASE anidado de T-SQL)
                CASE
                    WHEN desc_sn_arti > 0 THEN desc_sn_arti
                    WHEN desc_sn_fabr > 0 THEN desc_sn_fabr
                    WHEN desc_sn_grar > 0 THEN desc_sn_grar
                    WHEN desc_gsn_arti > 0 THEN desc_gsn_arti
                    WHEN desc_gsn_fabr > 0 THEN desc_gsn_fabr
                    WHEN desc_gsn_grar > 0 THEN desc_gsn_grar
                    ELSE 0.0
                END AS descuentoElegido
            FROM X0
        ),
        X2 AS (
            SELECT
                *,
                -- precioGD calculado una sola vez
                CASE
                    WHEN descuentoElegido > 0
                        THEN ROUND(precioLP * (100.0 - descuentoElegido) / 100.0, :priceDec)
                    ELSE 0.0
                END AS precioGD
            FROM X1
        )
        SELECT
            0.0 AS PrecioIGV,
            (SELECT Z0.Price FROM ArticuloPrecio Z0 WHERE Z0.ItemCode=:articulo AND Z0.PriceList=:listaPrecio LIMIT 1) AS PrecioUnitario,
            ROUND(
                CASE
                    WHEN precioSN > 0 THEN precioSN
                    WHEN precioGD > 0 THEN precioGD
                    WHEN precioPC > 0 THEN precioPC
                    WHEN precioPP > 0 THEN precioPP
                    ELSE precioLP
                END,
                :priceDec
            ) AS Precio,
            CASE
                WHEN precioLP > 0 AND (
                    CASE
                        WHEN precioSN > 0 THEN precioSN
                        WHEN precioGD > 0 THEN precioGD
                        WHEN precioPC > 0 THEN precioPC
                        WHEN precioPP > 0 THEN precioPP
                        ELSE precioLP
                    END
                ) < precioLP
                    THEN ROUND(((precioLP - (
                        CASE
                            WHEN precioSN > 0 THEN precioSN
                            WHEN precioGD > 0 THEN precioGD
                            WHEN precioPC > 0 THEN precioPC
                            WHEN precioPP > 0 THEN precioPP
                            ELSE precioLP
                        END
                    )) / precioLP) * 100.0, 0)
                ELSE 0.0
            END AS PorcentajeDescuento
        FROM X2;
    """)
    suspend fun getPrecioArticulo(
        fecha: String,
        articulo: String,
        listaPrecio: Int,
        cantidad: Int,
        cardCode: String,
        priceDec: Int
    ): ArticuloPedido




    @Query("""
        SELECT
            ROUND(((Z1.Rate/100) * :price) + :price, (SELECT PriceDec FROM Sociedad LIMIT 1))
            FROM Impuesto Z1
            WHERE Z1.Code = (SELECT DefaultTaxCode FROM Usuario LIMIT 1)
    """)
    suspend fun getPrecioAftVat(price: Double): Double


    @Query("""
        SELECT
            T0.PriceDec
        FROM Sociedad T0 LIMIT 1
    """)
    suspend fun getPriceDec(): Int

    @Query("""
        SELECT 
            T1.SlpName,
            T0.AccDocEntry,
            T0.CardName,
            T0.DocCur,
            T0.DocDate,
            T0.DocDueDate,
            T0.DocDueDate,
            T0.DocTotal,
            T0.DocTotal,
            T0.VatSum,
            T0.Comments,
            T0.DocStatus
        FROM ClientePedidos T0
        LEFT JOIN Vendedor T1 ON T0.SlpCode = T1.SlpCode
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun getPedidoInfo(accDocEntry: String): DoPedidoInfoView

    @Query("""
        SELECT
            * 
        FROM ClientePedidos 
        WHERE CANCELED = 'Y' AND DocDate = :fechaActual
    """)
    suspend fun getPedidosCancelados(fechaActual: String): List<ClientePedidosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clientePedidos: List<ClientePedidosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clientePedidos: ClientePedidosEntity)

    @Query("DELETE FROM ClientePedidos")
    suspend fun clearAll()

    @Update
    suspend fun update(clientePedidos: List<ClientePedidosEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnPedido(clientePedidos: ClientePedidosEntity)

    //Obtener un pedido por el AccDocEntry
    @Query("""
        SELECT 
            T0.AccDocEntry,
            T0.AccAction,
            T0.AccCreateDate,
            T0.AccCreateHour,
            T0.AccCreateUser,
            T0.AccError,
            T0.AccNotificado,
            T0.AccFinalized,
            T0.AccMigrated,
            T0.AccMovil,
            T0.AccUpdateDate,
            T0.AccUpdateHour,
            T0.AccUpdateUser,
            T0.Address,
            T0.Address2,
            T0.Authorized,
            T0.CANCELED,
            T0.CardCode,
            T0.AccDocEntrySN,
            T0.CardName,
            T0.CntctCode,
            T0.Comments,
            T0.DocCur,
            T0.DocDate,
            T0.DocDueDate,
            T0.DocEntry,
            T0.DocNum,
            T0.DocRate,
            T0.DocStatus,
            T0.DocTotal,
            T0.DocTotalFC,
            T0.GrosProfFC,
            T0.GrosProfit,
            T0.GroupNum,
            T0.Indicator,
            T0.LicTradNum,
            T0.NumAtCard,
            T0.PayToCode,
            T0.PriceList,
            T0.Project,
            T0.Series,
            T0.ShipToCode,
            T0.SlpCode,
            T0.TaxDate,
            T0.VatSum,
            T0.VatSumFC,
            T0.ObjType,
            T0.AccControl
        FROM ClientePedidos T0
        LEFT JOIN Vendedor T1 ON T0.SlpCode = T1.SlpCode 
        WHERE accDocEntry = :accDocEntry
    """)
    suspend fun getPedidoPorAccDocEntry(accDocEntry: String): ClientePedidosEntity

    //Obtener todos los pedidos NO MIGRADOS del cliente
    @Query("""
        SELECT 
        * 
        FROM ClientePedidos 
        WHERE AccMigrated = 'N' AND DocDate = :fechaActual 
        ORDER BY CardName 
    """)
    suspend fun getAllPedidosNoMigrados(fechaActual: String): List<ClientePedidosEntity>

    //Obtener todos los pedidos por CardCode
    @Query("SELECT * FROM ClientePedidos WHERE cardCode = :cardCode")
    suspend fun getAllPedidosPorCardCode(cardCode: String): List<ClientePedidosEntity>

        //Obtener todos los pedidos sin migrar
    @Query("SELECT * FROM ClientePedidos WHERE AccMigrated = 'N'")
    suspend fun getAllPedidosSinMigrar(): List<ClientePedidosEntity>


    @Query("SELECT * FROM ClientePedidos WHERE AccMigrated = 'N' AND DocDate = :fechaActual")
    suspend fun getAllPedidosNoMigradosFechaActual(fechaActual: String): List<ClientePedidosEntity>


    @Query("DELETE FROM ClientePedidos\n" +
            "WHERE DocDate NOT IN (:fechaActual)")
    suspend fun deleteAllPedidosFueraDeFechaActual(fechaActual: String)
}