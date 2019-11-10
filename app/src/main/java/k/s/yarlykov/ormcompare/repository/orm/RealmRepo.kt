package k.s.yarlykov.ormcompare.repository.orm

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.logIt
import k.s.yarlykov.ormcompare.rotate

class RealmRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : IRealmRepo {

    override fun loadUsers(multiplier : Int): Completable =
        Completable.fromSingle(
            GitHelper.getUsers()
                .doOnNext{
                    logIt("RealmRepo::doOnNext thread - ${Thread.currentThread().name}")
                }
                .firstOrError()
                .map { gitUsers ->
                    multiplyMap(gitUsers, multiplier, UserGit::toUserRealm)
                }
                .doOnSuccess { realmUsers ->
                    logIt("RealmRepo::doOnSuccess thread - ${Thread.currentThread().name}")
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









