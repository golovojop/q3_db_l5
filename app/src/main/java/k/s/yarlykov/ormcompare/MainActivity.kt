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

    private val layerLoading = 0
    private val layerContent = 1

    private val disposables = CompositeDisposable()

    private lateinit var ormRepo: IOrmRepo
    private lateinit var sqliteRepo: ISqliteRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ormRepo = OrmApp.getInstance().getOrmRepo()
        sqliteRepo = OrmApp.getInstance().getSqliteRepo()

        btnRealm.setOnClickListener {
            readRealm()
        }

        btnSql.setOnClickListener {
            readSqlite()
        }

        loadFromNetwork()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }


    private fun loadFromNetwork() {

        animator.displayedChild = layerLoading

        disposables.add(ormRepo.loadToRealm(count = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{animator.displayedChild = layerContent})

        disposables.add(sqliteRepo.loadToDb(count = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{animator.displayedChild = layerContent})
    }

    private fun readRealm() {

        val timeStart : Long = System.currentTimeMillis()

        disposables.add(
            // Чтение из базы в массив
            ormRepo.getUsers()
                .subscribeOn(Schedulers.computation())
                .map {li ->
                    Pair(timeStart, li)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::printRealmTime, ::printError)
        )
    }

    private fun readSqlite() {
        val timeStart : Long = System.currentTimeMillis()

        disposables.add(
            // Чтение из базы в массив
            sqliteRepo.getUsers()
                .subscribeOn(Schedulers.computation())
                .map {li ->
                    Pair(timeStart, li)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::printRealmTime, ::printError)
        )

    }

    private fun printRealmTime(data : Pair<Long, List<User>>) {
        val timeEnd = System.currentTimeMillis()
        printUsers(data.second)
        logIt("${data.second.size} records received from Realm for ${timeEnd - data.first} ms ")
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