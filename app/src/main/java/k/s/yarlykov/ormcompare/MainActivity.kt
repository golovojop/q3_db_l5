package k.s.yarlykov.ormcompare

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.application.OrmApp
import k.s.yarlykov.ormcompare.repository.IRepo
import k.s.yarlykov.ormcompare.repository.orm.IOrmRepo
import k.s.yarlykov.ormcompare.repository.sqlite.ISqliteRepo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val layerLoading = 0
    private val layerContent = 1

    private val progressBarMax = 200

    private val disposables = CompositeDisposable()

    private lateinit var ormRepo: IOrmRepo
    private lateinit var sqliteRepo: ISqliteRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pbRealm.max = progressBarMax
        pbSql.max = progressBarMax

        ormRepo = OrmApp.getInstance().getOrmRepo()
        sqliteRepo = OrmApp.getInstance().getSqliteRepo()

        btnRealm.setOnClickListener {
            readTest(ormRepo, createResultDrawer(pbRealm))
        }

        btnSql.setOnClickListener {
            readTest(sqliteRepo, createResultDrawer(pbSql))
        }

        loadFromNetwork()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()

        sqliteRepo.clearUsers()
    }

    private fun loadFromNetwork() {

        // Во сколько раз увеличить кол-во записей полученных из инета
        val recordsMultiplier = 30

        animator.displayedChild = layerLoading

        val realmObs = ormRepo.loadUsers(count = recordsMultiplier)
        val sqlObs = sqliteRepo.loadUsers(count = recordsMultiplier)

        disposables.add(
            Completable.mergeArray(realmObs, sqlObs)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{animator.displayedChild = layerContent}
        )
    }

    private fun readTest(repo : IRepo, resultHandler : (Long) -> Unit ) {

        val timeStart : Long = System.currentTimeMillis()

        disposables.add(
            // Чтение из базы в массив
            repo.getUsers()
                .subscribeOn(Schedulers.computation())
                .map {
                    timeStart
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultHandler, ::printError)
        )
    }

    // Создает функцию для отрисовки результата теста
    private fun createResultDrawer(view : ProgressBar) : (Long) -> Unit {

        return fun(timeStart : Long) {
            val timeEnd = System.currentTimeMillis()

            logIt("finished in ${(timeEnd - timeStart).toInt()} ms")
            view.progress = ((timeEnd - timeStart).toInt())
        }
    }

    private fun printError(t: Throwable) {
        logIt(t.message.toString())
    }
}