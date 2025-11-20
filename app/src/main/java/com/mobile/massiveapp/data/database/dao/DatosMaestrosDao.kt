package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao

@Dao
interface DatosMaestrosDao {

    /*@Transaction
    fun insertDataFromEndpoints(dataMap: Map<String, List<Any>>) {
        dataMap.forEach { (endpoint, dataList) ->
            val (daoClass, entityClass) = daos[endpoint]
                ?: throw IllegalArgumentException("Unknown endpoint: $endpoint")

            val dao = massiveDao.getDao(daoClass) as BaseDao<*>
            dao.clearAll()
            dao.insertAll(dataList.map { (it as entityClass).toDatabase() })
        }
    }*/
}