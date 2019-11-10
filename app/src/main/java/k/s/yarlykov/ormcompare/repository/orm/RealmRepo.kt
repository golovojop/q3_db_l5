package k.s.yarlykov.ormcompare.repository.orm

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.rotate

class RealmRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : IRealmRepo {

    override fun loadUsers(count: Int): Completable =
        Completable.fromSingle(
            GitHelper.getUsers()
                .firstOrError()
                .map { gitUsers ->
                    multiplyMap(gitUsers, UserGit::toUserRealm)
                }
                .doOnSuccess { realmUsers ->
                    dbRealm.insert(realmUsers)
                })

    //    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun getUsers(): Single<List<User>> {

        return Single.fromCallable {
            dbRealm.select()
        }
    }

    override fun clearUsers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}









