package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


internal class GetArticuloPorItemCodeUseCaseTest{

    @RelaxedMockK
    private lateinit var articuloRepository: ArticuloRepository

    lateinit var getArticuloUseCase: GetAllArticulosFromDatabaseUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getArticuloUseCase = GetAllArticulosFromDatabaseUseCase(articuloRepository)
    }

    @Test
    fun `when invoke then call repository`() = runBlocking{
        //Given
        coEvery { articuloRepository.getAllArticulosFromApi() } returns emptyList()

        //When
        getArticuloUseCase()

        //Then
        coVerify(exactly = 1) { articuloRepository.getAllArticulosFromDatabase() }
    }
}