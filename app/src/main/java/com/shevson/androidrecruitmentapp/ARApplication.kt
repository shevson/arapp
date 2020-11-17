package com.shevson.androidrecruitmentapp

import android.app.Application
import android.content.Context
import com.shevson.androidrecruitmentapp.di.ApiModule
import com.shevson.androidrecruitmentapp.di.DatabaseModule
import com.shevson.androidrecruitmentapp.di.HomeModule
import com.shevson.androidrecruitmentapp.di.HomeRepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Singleton

class ARApplication : Application(), HasAndroidInjector {

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private lateinit var mainApplicationComponent: MainApplicationComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector() = androidInjector

    private fun initDagger() {
        mainApplicationComponent = DaggerMainApplicationComponent.builder()
            .app(this)
            .build()

        mainApplicationComponent.inject(this)
    }
}

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DatabaseModule::class,
        ApiModule::class,
        HomeRepositoryModule::class,
        HomeModule::class
    ]
)
interface MainApplicationComponent {
    fun inject(app: ARApplication)

    @Component.Builder
    interface Builder {
        fun build(): MainApplicationComponent

        @BindsInstance
        fun app(app: Context): Builder
    }
}