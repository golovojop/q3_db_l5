package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.logIt
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GitHelper {

    private const val baseUrl = "https://api.github.com/"
    private const val HTTP_OK = 200

    private val api by lazy { initApiAdapter() }

    private val loadedUsers = BehaviorSubject.create<List<UserGit>>()


    fun load() {
//        val d = api.getUsers().subscribe {
//
//        }

        api.getUsers()
            .flatMapObservable { okHttpResponse ->

                logIt("Git response code = ${okHttpResponse.code()}")

                if (okHttpResponse.code() != HTTP_OK) {
                    throw Throwable("Can't receive Users list")
                }
                Observable.fromCallable {
                    logIt("git response length = ${okHttpResponse.body()!!.size}")
                    okHttpResponse.body()!!
                }
            }
            .doOnError{
                logIt("load:doOnError ${it.stackTrace.toString()}")
            }
            .subscribe(loadedUsers)
    }

    fun getUsers(): Observable<List<UserGit>> {
        return loadedUsers.hide()
    }

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