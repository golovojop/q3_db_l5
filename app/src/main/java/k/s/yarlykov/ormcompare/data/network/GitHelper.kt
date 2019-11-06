package k.s.yarlykov.ormcompare.data.network

import io.reactivex.Single
import k.s.yarlykov.ormcompare.domain.UserGit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GitHelper {

    private const val baseUrl = "https://api.github.com/"
    private const val HTTP_OK = 200

    private val api by lazy { initApiAdapter() }

    fun getUsers() : Single<List<UserGit>> {
        return api
            .getUsers()
            .flatMap {okHttpResponse ->
                if(okHttpResponse.code() != HTTP_OK) {
                    throw Throwable("Can't receive Users list")
                }
                Single.fromCallable {
                    okHttpResponse.body()!!
                }
            }
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