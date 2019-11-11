package k.s.yarlykov.ormcompare.repository.sqlite

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.logIt

class SqliteRepo(private val dbSql: DbProvider<UserSqlite, List<User>>) : ISqliteRepo {

    private val users = mutableListOf<UserSqlite>()
    private val completable = Completable.fromAction {
        dbSql.insert(users)
    }

    override fun loadUsers(dataSource: Observable<List<UserGit>>, multiplier: Int): Completable {
        dataSource
            .map { gitUsers ->
                logIt("SqliteRepo::loadUsers::map ${Thread.currentThread().name}")
                multiplyMap(gitUsers, multiplier, UserGit::toUserSqlite)
            }
            .doOnNext { sqliteUsers ->
                logIt("SqliteRepo::loadUsers::doOnNext ${Thread.currentThread().name}")
                users.addAll(sqliteUsers)
            }
            .subscribe()

        return completable
    }

    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            dbSql.select()
        }
    }

    override fun clearUsers() {
        dbSql.clear()
    }
}
