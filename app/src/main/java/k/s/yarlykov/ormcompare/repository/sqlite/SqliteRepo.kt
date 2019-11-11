package k.s.yarlykov.ormcompare.repository.sqlite

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.*

class SqliteRepo(private val dbSql: DbProvider<UserSqlite, List<User>>) : ISqliteRepo {

    override fun loadFromGithub(dataSource: Observable<List<UserGit>>, multiplier: Int): Completable =
        dataSource
            .map { gitUsers ->
                multiplyMap(gitUsers, multiplier, UserGit::toUserSqlite)
            }
            .doOnNext { sqliteUsers ->
                dbSql.insert(sqliteUsers)
            }
            .ignoreElements()


    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            dbSql.select()
        }
    }

    override fun clearUsers() {
        dbSql.clear()
    }
}
