package k.s.yarlykov.ormcompare.data.db.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserSqlite

class SqliteDbProvider(private val db: SQLiteDatabase) : DbProvider<UserSqlite, List<User>> {

    companion object {
        private const val tabName = "User"
        private const val colId = "id"
        private const val colLogin = "login"
        private const val colAvatar = "avatarUrl"
        private const val colReposUrl = "reposUrl"

        fun createTable(db: SQLiteDatabase) {

            val query = "CREATE TABLE " + tabName + " (" +
                    colId + " INTEGER PRIMARY KEY NOT NULL, " +
                    colLogin + " TEXT NOT NULL, " +
                    colAvatar + " TEXT, " +
                    colReposUrl + " TEXT)"
            db.execSQL(query)
        }
    }

    override fun insert(us: Iterable<UserSqlite>) {
        us.forEach { u ->
            insert(u)
        }
    }

    override fun insert(u: UserSqlite) {
        db.insert(tabName, null, initContentValues(u))
    }

    override fun update(u: UserSqlite) {
        db.update(tabName, initContentValues(u), "login = ${u.login}", null)
    }

    override fun delete(u: UserSqlite) {
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

    override fun clear() {
        db.execSQL("delete from $tabName")
    }

    private fun initContentValues(userSqlite: UserSqlite): ContentValues {
        return ContentValues().apply {
            this.put(colId, userSqlite.id)
            this.put(colLogin, userSqlite.login)
            this.put(colAvatar, userSqlite.avatarUrl)
            this.put(colReposUrl, userSqlite.reposUrl)
        }
    }
}