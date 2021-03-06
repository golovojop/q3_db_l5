package k.s.yarlykov.ormcompare.application

import android.app.Application
import io.reactivex.Observer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.di.component.AppComponent
import k.s.yarlykov.ormcompare.di.component.DaggerAppComponent
import k.s.yarlykov.ormcompare.di.module.*
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.logIt

class OrmApp : Application() {

    companion object {
        const val baseUrl = "https://api.github.com/"
        const val dbName = "q3.db"
        const val dbVersion = 1
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(baseUrl))
            .ormRealmModule(OrmRealmModule(dbName))
            .ormRoomModule(OrmRoomModule(dbName))
            .sqliteModule(SqliteModule(dbName, dbVersion))
            .build()
    }
}