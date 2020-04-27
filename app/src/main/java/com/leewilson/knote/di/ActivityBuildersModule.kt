package com.leewilson.knote.di

import com.leewilson.knote.di.main.MainFragmentBuildersModule
import com.leewilson.knote.di.main.MainModule
import com.leewilson.knote.ui.main.MainActivity
import com.leewilson.knote.ui.splash.SplashActivity
import com.leewilson.knote.di.main.MainScope
import com.leewilson.knote.di.splash.SplashScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @SplashScope
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuildersModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}