package k.s.yarlykov.ormcompare.application

import android.app.Application
import k.s.yarlykov.ormcompare.di.component.AppComponent
import k.s.yarlykov.ormcompare.di.component.DaggerAppComponent
import k.s.yarlykov.ormcompare.di.module.AppModule
import k.s.yarlykov.ormcompare.di.module.OrmRealmModule
import k.s.yarlykov.ormcompare.di.module.SqliteModule

class OrmApp : Application() {

    companion object {
        const val dbName = "q3.db"
        const val dbVersion = 1
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .ormRealmModule(OrmRealmModule(dbName))
            .sqliteModule(SqliteModule(dbName, dbVersion))
//            .networkModule(NetworkModule(baseUrl))
            .build()
    }
}