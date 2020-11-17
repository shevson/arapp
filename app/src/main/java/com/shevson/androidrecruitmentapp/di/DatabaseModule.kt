package com.shevson.androidrecruitmentapp.di

import android.content.Context
import com.shevson.androidrecruitmentapp.data.local.LocalDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideLocalDatabase(context: Context): LocalDb {
        return LocalDb.init(context)
    }

    @Provides
    @JvmStatic
    fun provideUsersDao(db: LocalDb) = db.usersDao()

    @Provides
    @JvmStatic
    fun provideReposDao(db: LocalDb) = db.reposDao()

    @Provides
    @JvmStatic
    fun provideUsersReposJoinDao(db: LocalDb) = db.usersReposDao()
}