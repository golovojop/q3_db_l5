package k.s.yarlykov.ormcompare.data.network

import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.logIt
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GitHelper(val gitApi: GitApi) {

    fun getUsers(): ConnectableObservable<List<UserGit>> =
        gitApi.getUsers()
            .map { okHttpResponse ->
                logIt("GitHelper::getUsers okHttpResponse.code = ${okHttpResponse.code()}")
                if (!okHttpResponse.isSuccessful) {
                    throw Throwable("Can't receive Users list")
                }
                okHttpResponse.body()!!
            }
            .subscribeOn(Schedulers.io())
            .doOnNext {
                logIt("GitHelper::getUsers::doOnNext ${Thread.currentThread().name}")
            }
            .replay()
}