package k.s.yarlykov.ormcompare

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.application.OrmApp
import k.s.yarlykov.ormcompare.data.network.GitHelper
import k.s.yarlykov.ormcompare.repository.IRepo
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    private val layerLoading = 0
    private val layerContent = 1

    private val progressBarMax = 200

    // Во сколько раз увеличить кол-во записей полученных из инета
    private val recordsMultiplier = 30

    private val disposables = CompositeDisposable()

    @Inject
    @field:Named("realm_repo")
    lateinit var realmRepo: IRepo

    @Inject
    @field:Named("room_repo")
    lateinit var roomRepo: IRepo

    @Inject
    @field:Named("sqlite_repo")
    lateinit var sqliteRepo: IRepo

    @Inject
    lateinit var gitHelper : GitHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as OrmApp).appComponent.inject(this)

        initViews()
        loadFromNetwork()
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun loadFromNetwork() {

        animator.displayedChild = layerLoading

        val dataSource = gitHelper.getUsers()

        disposables.add(
            Completable.mergeArray(
                realmRepo.loadFromGithub(dataSource, recordsMultiplier),
                roomRepo.loadFromGithub(dataSource, recordsMultiplier),
                sqliteRepo.loadFromGithub(dataSource, recordsMultiplier))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{animator.displayedChild = layerContent}
        )

        dataSource.connect()
    }

    private fun executeReadTest(repo : IRepo, resultHandler : (Pair<Int, Long>) -> Unit ) {

        val timeStart : Long = System.currentTimeMillis()
        var records = 0

        disposables.add(
            // Чтение из базы в массив
            repo.getUsers()
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    records = it.size
                }
                .map {
                    Pair(records, timeStart)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultHandler, ::printError)
        )
    }

    // Создает функцию для отрисовки результата теста
    private fun createResultDrawer(pbView : ProgressBar, tView : TextView) : (Pair<Int, Long>) -> Unit {

        return fun(data : Pair<Int, Long>) {
            val timeEnd = System.currentTimeMillis()

            val result = "Read test:\n\n${data.first} records\n${(timeEnd - data.second).toInt()} ms"
            tView.text = result

            logIt("finished in ${(timeEnd - data.second).toInt()} ms")
            pbView.progress = ((timeEnd - data.second).toInt())
        }
    }

    private fun printError(t: Throwable) {
        logIt(t.message.toString())
    }

    private fun initViews() {
        setTitle(R.string.app_title)

        pbRealm.max = progressBarMax
        pbSql.max = progressBarMax
        pbRoom.max = progressBarMax

        btnRealm.setOnClickListener {
            executeReadTest(realmRepo, createResultDrawer(pbRealm, tvRealm))
        }

        btnRoom.setOnClickListener{
            executeReadTest(roomRepo, createResultDrawer(pbRoom, tvRoom))
        }

        btnSql.setOnClickListener {
            executeReadTest(sqliteRepo, createResultDrawer(pbSql, tvSql))
        }
    }
}