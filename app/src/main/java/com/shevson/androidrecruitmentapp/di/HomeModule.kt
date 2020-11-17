package com.shevson.androidrecruitmentapp.di

import com.shevson.androidrecruitmentapp.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface HomeModule {

    @ContributesAndroidInjector
    fun homeActivity(): HomeActivity
}


