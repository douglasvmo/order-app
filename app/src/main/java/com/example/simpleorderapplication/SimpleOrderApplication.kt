package com.example.simpleorderapplication

import android.app.Application
import com.example.simpleorderapplication.di.databaseModule
import com.example.simpleorderapplication.di.repositoryModule
import com.example.simpleorderapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class SimpleOrderApplication: Application() {
   override fun onCreate(){
        super.onCreate()

       startKoin {
           androidContext(this@SimpleOrderApplication)
           androidLogger(Level.DEBUG)
           modules(databaseModule, repositoryModule, viewModelModule)
       }
    }
}