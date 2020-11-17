package com.shevson.androidrecruitmentapp.di

import com.shevson.androidrecruitmentapp.data.repositories.HomeRepository
import com.shevson.androidrecruitmentapp.data.repositories.HomeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface HomeRepositoryModule {
    @Suppress("UNUSED")
    @Binds
    fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}