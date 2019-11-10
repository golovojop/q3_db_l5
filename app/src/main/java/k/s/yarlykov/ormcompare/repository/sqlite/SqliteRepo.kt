package k.s.yarlykov.ormcompare.repository.sqlite

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.rotate

class SqliteRepo(private val dbSql: DbProvider<UserSqlite, List<User>>) : ISqliteRepo {

    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            dbSql.select()
        }
    }

    override fun loadUsers(count: Int): Completable =
         Completable.fromSingle(
            GitHelper.getUsers()
                .firstOrError()
                .map { gitUsers ->
                    multiplyMap(gitUsers, UserGit::toUserSqlite)
                }
                .doOnSuccess { sqliteUsers ->
                    dbSql.insert(sqliteUsers)
//                    sqliteUsers.forEach {u ->
//                        dbSql.insert(u)
//                    }
                }
        )

    override fun clearUsers() {
        dbSql.clear()
    }
}
