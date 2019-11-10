package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.ormcompare.domain.UserGit
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GitHelper {

    private const val baseUrl = "https://api.github.com/"

    private val api by lazy { initApiAdapter() }

    private val loadedUsers = BehaviorSubject.create<List<UserGit>>()

    init {
        api.getUsers()
            .subscribeOn(Schedulers.io())
            .flatMapObservable { okHttpResponse ->
                if (!okHttpResponse.isSuccessful) {
                    throw Throwable("Can't receive Users list")
                }
                Observable.fromCallable {
                    okHttpResponse.body()!!
                }
            }
//            .observeOn(AndroidSchedulers.mainThread())
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