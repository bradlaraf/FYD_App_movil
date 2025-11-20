package com.mobile.massiveapp.domain.model

data class DoArticuloPedidoInfo(
    val ItemName: String,
    val SalUnitMsr: String,
    val ItemCode: String,
    val price: Double,
    val UgpName: String,
    val UomName: String,
    val UomCode: String,
    val UomEntry: Int,
) {
    constructor() : this("", "", "", 0.0, "", "","", 0)
}

data class DoPedidoDetalle(
    /*** Datos de Lectura ***/
    val ItemName: String,
    val ItemCode: String,
    val Price: Double,
    val LineTotal: Double,
    val Quantity: Double,
    val UgpName: String,    //Grupo Unidad Medida
    val UomName: String,    //Unidad medida
    val Impuesto: String,
    val Almacen: String,
    val ListaPrecio: String,
    /*** Codigos ***/
    val WhsCode: String,
    val PriceList: Int,
    val TaxCode: String,
    val UomCode: String,    //Codigo GrupoUM
    val UomEntry: Int,      //Codigo Unidad Medida
) {
    constructor(): this(
        ItemName = "",
        ItemCode = "",
        Price = 0.0,
        LineTotal = 0.0,
        Quantity = 0.0,
        UgpName = "",
        UomName = "",
        Impuesto = "",
        Almacen = "",
        ListaPrecio = "",
        WhsCode = "",
        PriceList = 0,
        TaxCode = "",
        UomCode = "",
        UomEntry = 0
    )
}
