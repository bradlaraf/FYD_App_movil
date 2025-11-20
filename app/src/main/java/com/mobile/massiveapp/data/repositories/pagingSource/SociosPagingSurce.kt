package com.mobile.massiveapp.data.repositories.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity

class SociosPagingSurce(
    val backend: ClienteSociosDao
): PagingSource<Int, ClienteSociosEntity>() {

    override fun getRefreshKey(state: PagingState<Int, ClienteSociosEntity>): Int? {
        return null
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ClienteSociosEntity> {
        return try {
            val page = params.key ?: 1
            val response = backend.getAllSocios()
            LoadResult.Page(
                data = response,
                prevKey = if(page ==1 ) null else -1,
                nextKey = page.plus(1)
            )
        } catch (e:Exception){
            return LoadResult.Error(e)
        }
    }
}

class SociosPagingSource(
    private val sociosDao: ClienteSociosDao
) : PagingSource<Int, ClienteSociosEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClienteSociosEntity> {
        return try {
            val page = params.key ?: 0 // Número de página
            val pageSize = params.loadSize // Tamaño de página
            val offset = page * pageSize

            val socios = sociosDao.getAllSociosPaging(pageSize, offset)

            LoadResult.Page(
                data = socios,
                prevKey = if (page == 0) null else page - 1, // Clave de página anterior
                nextKey = if (socios.isEmpty()) null else page + 1 // Clave de página siguiente
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClienteSociosEntity>): Int? {
        return null
    }
}