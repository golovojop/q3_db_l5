package k.s.yarlykov.ormcompare.repository.sqlite

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.User

interface ISqliteRepo {
    fun getUsers(): Single<List<User>>
    fun loadToDb(count : Int = 1) : Completable
}