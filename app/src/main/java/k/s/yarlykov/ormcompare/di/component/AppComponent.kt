package k.s.yarlykov.ormcompare.di.component

import dagger.Component
import k.s.yarlykov.ormcompare.MainActivity
import k.s.yarlykov.ormcompare.di.module.AppModule
import k.s.yarlykov.ormcompare.di.module.OrmRealmModule
import k.s.yarlykov.ormcompare.di.module.OrmRoomModule
import k.s.yarlykov.ormcompare.di.module.SqliteModule
import javax.inject.Singleton

@Singleton
@Component(modules=[AppModule::class, OrmRealmModule::class, SqliteModule::class, OrmRoomModule::class/*, NetworkModule::class*/])

interface AppComponent {
    fun inject(activity : MainActivity)
}