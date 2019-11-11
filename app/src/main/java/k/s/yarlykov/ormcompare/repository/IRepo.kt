package k.s.yarlykov.ormcompare.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserGit

interface IRepo {
    fun getUsers(): Single<List<User>>
    fun loadFromGithub(dataSource: Observable<List<UserGit>>, multiplier : Int = 1) : Completable
    fun clearUsers()
}

