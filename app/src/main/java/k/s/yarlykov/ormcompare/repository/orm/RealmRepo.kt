package k.s.yarlykov.ormcompare.repository.orm

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.logIt

class RealmRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : IRealmRepo {

    override fun loadFromGithub(dataSource: Observable<List<UserGit>>, multiplier: Int): Completable =
        dataSource
            .map { gitUsers ->
                multiplyMap(gitUsers, multiplier, UserGit::toUserRealm)
            }
            .doOnNext { realmUsers ->
                dbRealm.insert(realmUsers)
            }
            .ignoreElements()

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









