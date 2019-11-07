package k.s.yarlykov.ormcompare.repository.sqlite

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.domain.UserRealm
import k.s.yarlykov.ormcompare.domain.toUserRealm
import k.s.yarlykov.ormcompare.rotate

class SqliteRepo(private val dbSql: DbProvider<UserRealm, List<User>>) : ISqliteRepo {

    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            dbSql.select()
        }
    }

    override fun loadToDb(count: Int): Completable = Completable.fromSingle(
        GitHelper.getUsers()
            .firstOrError()
            .doOnSuccess { gitUsers ->

                // Для теста увеличиваю количество записей в count раз.
                // При каждой итерации меняю поле login цикличным сдвигом символов и добавлением id
                val step = gitUsers.map { it.id }.max()!!
                (0 until count).forEach { i ->
                    gitUsers.forEach { user ->
                        UserGit().apply {
                            id = user.id + (i * step)
                            login = "${user.login.rotate(1)}_$id"
                            avatarUrl = user.avatarUrl
                            reposUrl = user.reposUrl

                            dbSql.insert(this.toUserRealm())
                        }
                    }
                }
            })
}
