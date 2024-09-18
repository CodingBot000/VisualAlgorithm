package com.algorithm.di

import com.algorithm.domain.repository.graph.GraphRepository
import com.algorithm.domain.repository.graph.GraphRepositoryImpl
import com.algorithm.domain.repository.sorting.SortingHeapRepository
import com.algorithm.domain.repository.sorting.SortingHeapRepositoryImpl
import com.algorithm.domain.repository.sorting.SortingRepository
import com.algorithm.domain.repository.sorting.SortingRepositoryImpl
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
