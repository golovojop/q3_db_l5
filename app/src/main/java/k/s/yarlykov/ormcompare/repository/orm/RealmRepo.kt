package k.s.yarlykov.ormcompare.repository.orm

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.logIt

class RealmRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : IRealmRepo {

    private val users = mutableListOf<UserRealm>()
    private val completable = Completable.fromAction {
        dbRealm.insert(users)
    }

    override fun loadUsers(dataSource: Observable<List<UserGit>>, multiplier : Int): Completable {
            dataSource
                .map { gitUsers ->
                    logIt("RealmRepo::loadUsers::map ${Thread.currentThread().name}")
                    multiplyMap(gitUsers, multiplier, UserGit::toUserRealm)
                }
                .doOnNext { realmUsers ->
                    logIt("RealmRepo::loadUsers::doOnNext ${Thread.currentThread().name}")
                    users.addAll(realmUsers)
                }
                .subscribe()

            return completable
        }

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









