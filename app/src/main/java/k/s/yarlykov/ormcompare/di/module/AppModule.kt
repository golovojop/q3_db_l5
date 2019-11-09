package k.s.yarlykov.ormcompare.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import k.s.yarlykov.ormcompare.application.OrmApp
import javax.inject.Singleton

@Module
class AppModule (private val app : OrmApp) {

    @Provides
    @Singleton
    fun provideApplication() : OrmApp = app

    @Provides
    @Singleton
    fun provideContext() : Context = app
}