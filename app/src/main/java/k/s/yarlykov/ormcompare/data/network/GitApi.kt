package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Single
import k.s.yarlykov.ormcompare.data.model.github.GitUser
import retrofit2.Response
import retrofit2.http.GET

interface GitApi {
    @GET("users")
    fun getUsers() : Single<Response<List<GitUser>>>
}



