package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Observable
import k.s.yarlykov.ormcompare.domain.UserGit
import retrofit2.Response
import retrofit2.http.GET

interface GitApi {
    @GET("users")
    fun getUsers() : Observable<Response<List<UserGit>>>
}