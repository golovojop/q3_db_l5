package k.s.yarlykov.ormcompare.application

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration

class OrmApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)

        val realmConfig = RealmConfiguration
            .Builder()
            .name("realm_realm")
            .build()

        Realm.setDefaultConfiguration(realmConfig)
    }
}