package k.s.yarlykov.ormcompare.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import k.s.yarlykov.ormcompare.data.db.orm.RealmDbProvider
import k.s.yarlykov.ormcompare.data.db.sqlite.SqliteHelper
import k.s.yarlykov.ormcompare.di.component.AppComponent
import k.s.yarlykov.ormcompare.di.component.DaggerAppComponent
import k.s.yarlykov.ormcompare.di.module.AppModule
import k.s.yarlykov.ormcompare.di.module.OrmRealmModule
import k.s.yarlykov.ormcompare.repository.IRepo
import k.s.yarlykov.ormcompare.repository.orm.OrmRepo
import k.s.yarlykov.ormcompare.repository.sqlite.SqliteRepo

class OrmApp : Application() {

    companion object {
        const val dbName = "q3.db"
        const val dbVersion = 1
        private lateinit var instance: OrmApp
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .ormRealmModule(OrmRealmModule(dbName))
//            .networkModule(NetworkModule(baseUrl))
            .build()
    }


    fun getOrmRepo(): IRepo = ormRepository

    fun getSqliteRepo(): IRepo = sqliteRepository

    // Realm хранит базенку в папке data/data/files/<db_name>
    private val ormRepository: IRepo by lazy {
        Realm.init(this)
        val realmConfig = RealmConfiguration
            .Builder()
            .name(dbName)
            .build()

        Realm.setDefaultConfiguration(realmConfig)
        OrmRepo(RealmDbProvider())
    }

    // SQLite хранит базенку в папке data/data/databases/<db_name>
    private val sqliteRepository: IRepo by lazy {
        SqliteRepo(SqliteHelper(this, dbName, null, dbVersion).createDbProvider())
    }
}