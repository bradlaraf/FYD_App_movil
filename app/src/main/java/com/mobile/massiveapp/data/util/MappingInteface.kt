package com.mobile.massiveapp.data.util

abstract class MappingInteface<T> {
    abstract fun map(data: List<T>):List<*>

    abstract fun listOfKeys(data: List<T>): List<Any>
}

data class Perro (
    val Id: String,
    val Nombre: String
    )

data class Test(
    val Id: String,
    val Nombre: String
) : MappingInteface<Test>() {
    override fun map(data: List<Test>): List<Perro> {
        return data.map {
        Perro(
            Id = it.Id,
            Nombre = it.Nombre
            )
        }
    }

    override fun listOfKeys(data: List<Test>): List<Any> {
        return data.map { it.Id }
    }
}

fun test(data: List<Any>){

}

inline fun <reified T : MappingInteface<T>> getMap(instance: T, data: List<*>): List<*> {
    // Verificar y castear los elementos de la lista al tipo esperado
    @Suppress("UNCHECKED_CAST")
    val typedData = data.filterIsInstance(instance::class.java as Class<T>)
    return instance.map(typedData as List<T>)
}

inline fun <reified T : MappingInteface<T>> getKeys(instance: T, data: List<Any>): List<Any> {
    // Verificar y castear los elementos de la lista al tipo esperado
    @Suppress("UNCHECKED_CAST")
    val typedData = data.filterIsInstance(instance::class.java as Class<T>)
    return instance.listOfKeys(typedData as List<T>) as List<Any>
}

