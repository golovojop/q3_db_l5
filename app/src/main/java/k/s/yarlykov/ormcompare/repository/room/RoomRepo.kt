package k.s.yarlykov.ormcompare.repository.room

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.db.room.UserDao
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.domain.*

class RoomRepo(val userDao: UserDao) : IRoomRepo {

    override fun getUsers(): Single<List<User>> {
        return Single.fromCallable {
            userDao.select()
        }
    }

    override fun loadUsers(count: Int): Completable =
        Completable.fromSingle(
            GitHelper.getUsers()
                .firstOrError()
                .map { gitUsers ->
                    multiplyMap(gitUsers, UserGit::toUserRoom)
                }
                .doOnSuccess { roomUsers ->
                    roomUsers.forEach { u ->
                        userDao.insert(u)
                    }
                }
        )

    override fun clearUsers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}