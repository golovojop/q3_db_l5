package k.s.yarlykov.ormcompare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import k.s.yarlykov.ormcompare.domain.UserGit
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.data.orm.RealmDbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.repository.OrmRepo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val disposables = CompositeDisposable()


    private val ormRepo by lazy {
        Realm.init(this)
        OrmRepo(RealmDbProvider())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposables.add(ormRepo.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::printUsers, ::printError))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun printUsers(list: List<User>) {
        tvInfo.text = list.map {
            "${it.id} ${it.login}"
        }.joinToString("\n")

    }

    private fun printError(t: Throwable) {
        tvInfo.text = t.message.toString()
        logIt(t.message.toString())
    }
}
