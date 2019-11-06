package k.s.yarlykov.ormcompare.repository

import io.reactivex.Completable
import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserGit

interface IOrmRepo {

    fun getUsers(): Single<List<User>>
    fun loadToRealm() : Completable
}