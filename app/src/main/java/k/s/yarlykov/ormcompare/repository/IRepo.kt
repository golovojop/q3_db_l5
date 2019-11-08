package k.s.yarlykov.ormcompare.repository

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.User


interface IRepo {
    fun getUsers(): Single<List<User>>
    fun loadUsers(count : Int = 1) : Completable
    fun clearUsers()
}

