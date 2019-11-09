package k.s.yarlykov.ormcompare.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import k.s.yarlykov.ormcompare.data.db.sqlite.SqliteHelper
import k.s.yarlykov.ormcompare.repository.IRepo
import k.s.yarlykov.ormcompare.repository.sqlite.SqliteRepo
import javax.inject.Named
import javax.inject.Singleton

/**
 * SQLite хранит базенку в папке data/data/databases/<db_name>
 */

@Module
class SqliteModule (private val dbName: String, private val dbVersion : Int) {

    @Singleton
    @Provides
    fun provideSqliteHelper(context : Context) : SqliteHelper =
        SqliteHelper(context, dbName, null, dbVersion)

    @Provides
    @Named("sqlite_repo")
    @Singleton
    fun provideSqliteRepo(helper : SqliteHelper) : IRepo =
        SqliteRepo(helper.createDbProvider())
}