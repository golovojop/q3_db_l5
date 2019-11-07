package k.s.yarlykov.ormcompare.data.db.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm

class SqliteDbProvider(private val db: SQLiteDatabase) : DbProvider<UserRealm, List<User>> {

    private val tabName = "UserRealm"
    private val colId = "id"
    private val colLogin = "login"
    private val colAvatar = "avatarUrl"
    private val colReposUrl = "reposUrl"

    fun createTable(db: SQLiteDatabase) {

        val query = "CREATE TABLE " + tabName + " (" +
                colId + " INTEGER PRIMARY KEY NOT NULL, " +
                colLogin + " TEXT NOT NULL, " +
                colAvatar + " TEXT, " +
                colReposUrl + " TEXT"
        db.execSQL(query)
    }

    override fun insert(u: UserRealm) {
        db.insert(tabName, null, initContentValues(u))
    }

    override fun update(u: UserRealm) {
        db.update(tabName, initContentValues(u), "login = ${u.login}", null)
    }

    override fun delete(u: UserRealm) {
        db.delete(tabName, "login = ${u.login}", null)
    }

    override fun select(): List<User> {

        val query = "select * from $tabName"
        val li = mutableListOf<User>()

        db.rawQuery(query, null).use { cursor ->

            val indexId = cursor.getColumnIndex(colId)
            val indexLogin = cursor.getColumnIndex(colLogin)
            val indexAvatar = cursor.getColumnIndex(colAvatar)
            val indexRepo = cursor.getColumnIndex(colReposUrl)

            if (cursor.moveToNext()) {
                do {
                    li.add(
                        User(
                            cursor.getInt(indexId),
                            cursor.getString(indexLogin),
                            cursor.getString(indexAvatar),
                            cursor.getString(indexRepo)
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return li
    }

    private fun initContentValues(userRealm: UserRealm): ContentValues {
        return ContentValues().apply {
            this.put(colId, userRealm.id)
            this.put(colLogin, userRealm.login)
            this.put(colAvatar, userRealm.avatarUrl)
            this.put(colReposUrl, userRealm.reposUrl)
        }
    }
}