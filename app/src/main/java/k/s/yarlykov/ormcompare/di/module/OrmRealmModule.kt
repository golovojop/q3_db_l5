package k.s.yarlykov.ormcompare.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import k.s.yarlykov.ormcompare.application.OrmApp
import k.s.yarlykov.ormcompare.data.db.orm.RealmDbProvider
import k.s.yarlykov.ormcompare.repository.IRepo
import k.s.yarlykov.ormcompare.repository.orm.OrmRepo
import javax.inject.Singleton

@Module
class OrmRealmModule(private val dbName: String) {

    @Singleton
    @Provides
    fun provideRealmConfiguration(context: Context): RealmConfiguration {

        Realm.init(context)

        return RealmConfiguration
            .Builder()
            .name(OrmApp.dbName)
            .build()
    }

    @Singleton
    @Provides
    fun provideRealmDbProvider(): RealmDbProvider = RealmDbProvider()

    @Singleton
    @Provides
    fun provideRealmRepository(
        context: Context,
        realmConfig: RealmConfiguration,
        realmDbProvider: RealmDbProvider
    ): IRepo {
        Realm.init(context)
        Realm.setDefaultConfiguration(realmConfig)
        return OrmRepo(realmDbProvider)

    }

}