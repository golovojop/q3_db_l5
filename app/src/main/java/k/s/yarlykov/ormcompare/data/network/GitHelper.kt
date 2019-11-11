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

object GitHelper {

    private const val baseUrl = "https://api.github.com/"

    private val api by lazy { initApiAdapter() }

    fun getUsers(): ConnectableObservable<List<UserGit>> =
        api.getUsers()
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

    private fun initApiAdapter(): GitApi {
        // Установить таймауты
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val adapter = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return adapter.create(GitApi::class.java)
    }
}