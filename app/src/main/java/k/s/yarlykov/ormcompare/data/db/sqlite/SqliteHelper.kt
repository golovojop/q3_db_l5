package k.s.yarlykov.ormcompare.data.db.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm

class SqliteHelper(
    context: Context?,
    dbName: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, dbName, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        SqliteDbProvider.createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createDbProvider() : DbProvider<UserRealm, List<User>> = SqliteDbProvider(writableDatabase)
}