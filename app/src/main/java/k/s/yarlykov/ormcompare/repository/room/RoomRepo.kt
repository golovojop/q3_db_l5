package k.s.yarlykov.ormcompare.repository.room

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.room.RoomDbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.domain.multiplyMap
import k.s.yarlykov.ormcompare.domain.toUserRoom

class RoomRepo(private val userDao: RoomDbProvider) : IRoomRepo {

    override fun loadFromGithub(dataSource: Observable<List<UserGit>>, multiplier: Int): Completable =
        dataSource
            .map { gitUsers ->
                multiplyMap(gitUsers, multiplier, UserGit::toUserRoom)
            }
            .doOnNext { roomUsers ->
                userDao.insert(roomUsers)
            }
            .ignoreElements()


    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            userDao.select()
        }
    }

    override fun clearUsers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}