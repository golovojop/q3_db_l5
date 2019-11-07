package k.s.yarlykov.ormcompare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.application.OrmApp
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.repository.orm.IOrmRepo
import k.s.yarlykov.ormcompare.repository.sqlite.ISqliteRepo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var ormRepo: IOrmRepo
    private lateinit var sqliteRepo: ISqliteRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ormRepo = OrmApp.getInstance().getOrmRepo()
        sqliteRepo = OrmApp.getInstance().getSqliteRepo()

        val d = ormRepo
            // Запись в базу из сети
            .loadToRealm()
            .subscribeOn(Schedulers.io())
            .subscribe {
                disposables.add(
                    // Чтение из базы в массив
                    ormRepo.getUsers()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(::printUsers, ::printError)
                )
            }

        disposables.add(d)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun printUsers(list: List<User>) {
        tvInfo.text = list
            .asSequence()
            .take(20)
            .map {
            "${it.id}: ${it.login}"
        }.joinToString("\n")

        list.forEach {
            logIt("${it.id}: ${it.login}")
        }
    }

    private fun printError(t: Throwable) {
        tvInfo.text = t.message.toString()
        logIt(t.message.toString())
    }
}