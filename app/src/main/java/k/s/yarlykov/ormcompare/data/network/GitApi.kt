package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.UserGit
import retrofit2.Response
import retrofit2.http.GET

interface GitApi {
    @GET("users")
    fun getUsers() : Single<Response<List<UserGit>>>
}



