package k.s.yarlykov.ormcompare.application

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration
import k.s.yarlykov.ormcompare.data.db.orm.RealmDbProvider
import k.s.yarlykov.ormcompare.data.db.sqlite.SqliteHelper
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.di.AppComponent
import k.s.yarlykov.ormcompare.di.AppModule
import k.s.yarlykov.ormcompare.di.DaggerAppComponent
import k.s.yarlykov.ormcompare.di.NetworkModule
import k.s.yarlykov.ormcompare.logIt
import k.s.yarlykov.ormcompare.repository.orm.IOrmRepo
import k.s.yarlykov.ormcompare.repository.orm.OrmRepo
import k.s.yarlykov.ormcompare.repository.sqlite.ISqliteRepo
import k.s.yarlykov.ormcompare.repository.sqlite.SqliteRepo

class OrmApp : Application() {

    companion object {
        const val dbName = "q3.db"
        const val baseUrl = "https://api.github.com/"
        const val dbVersion = 1
        private lateinit var instance: OrmApp
        fun getInstance() = instance
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
//            .networkModule(NetworkModule(baseUrl))
            .build()
    }


    fun getOrmRepo(): IOrmRepo = ormRepository

    fun getSqliteRepo(): ISqliteRepo = sqliteRepository

    // Realm хранит базенку в папке data/data/files/<db_name>
    private val ormRepository: IOrmRepo by lazy {
        Realm.init(this)
        val realmConfig = RealmConfiguration
            .Builder()
            .name(dbName)
            .build()

        Realm.setDefaultConfiguration(realmConfig)
        OrmRepo(RealmDbProvider())
    }

    // SQLite хранит базенку в папке data/data/databases/<db_name>
    private val sqliteRepository: ISqliteRepo by lazy {
        SqliteRepo(SqliteHelper(this, dbName, null, dbVersion).createDbProvider())
    }
}