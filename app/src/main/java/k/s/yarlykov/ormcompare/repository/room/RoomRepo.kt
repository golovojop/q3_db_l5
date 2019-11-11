package k.s.yarlykov.ormcompare.repository.room

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.room.RoomDbProvider
import k.s.yarlykov.ormcompare.domain.*
import k.s.yarlykov.ormcompare.logIt

class RoomRepo(private val userDao: RoomDbProvider) : IRoomRepo {

    private val users = mutableListOf<UserRoom>()
    private val completable = Completable.fromAction {
        userDao.insert(users)
    }

    override fun loadUsers(dataSource: Observable<List<UserGit>>, multiplier: Int): Completable {
        dataSource
            .map { gitUsers ->
                logIt("RoomRepo::loadUsers::map ${Thread.currentThread().name}")
                multiplyMap(gitUsers, multiplier, UserGit::toUserRoom)
            }
            .doOnNext { roomUsers ->
                logIt("RoomRepo::loadUsers::doOnNext ${Thread.currentThread().name}")
                users.addAll(roomUsers)
            }
            .subscribe()

        return completable
    }

    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            userDao.select()
        }
    }

    override fun clearUsers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}