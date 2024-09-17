package com.algorithm.di

import com.algorithm.domain.repository.GraphRepository
import com.algorithm.domain.repository.GraphRepositoryImpl
import com.algorithm.domain.repository.SortingHeapRepository
import com.algorithm.domain.repository.SortingHeapRepositoryImpl
import com.algorithm.domain.repository.SortingRepository
import com.algorithm.domain.repository.SortingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindSortingRepository(soringRepository: SortingRepositoryImpl): SortingRepository

    @Binds
    @Singleton
    fun bindSortingHeapRepository(soringHeapRepository: SortingHeapRepositoryImpl): SortingHeapRepository

    @Binds
    @Singleton
    fun bindGraphRepository(graphRepository: GraphRepositoryImpl): GraphRepository
}
