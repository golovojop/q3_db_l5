package k.s.yarlykov.ormcompare.repository

import android.os.Build
import androidx.annotation.RequiresApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.data.orm.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm
import k.s.yarlykov.ormcompare.domain.toUser
import k.s.yarlykov.ormcompare.domain.toUserRealm

class OrmRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : IOrmRepo {

//    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun getUsers(): Single<List<User>> {

        return GitHelper.getUsers()
            .subscribeOn(Schedulers.io())
            .doOnSuccess { gitUsers ->
                gitUsers.forEach { user ->
                    dbRealm.insert(user.toUserRealm())
                }
            }
            .map { gitUsers ->
                gitUsers
                    .asSequence()
                    .map { gitUser ->
                        gitUser.toUser()
                    }
                    .toList()
            }
    }
}