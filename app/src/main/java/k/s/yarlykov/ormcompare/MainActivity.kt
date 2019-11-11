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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as OrmApp).appComponent.inject(this)

        setTitle(R.string.app_title)

        pbRealm.max = progressBarMax
        pbSql.max = progressBarMax
        pbRoom.max = progressBarMax

        btnRealm.setOnClickListener {
            readTest(realmRepo, createResultDrawer(pbRealm, tvRealm))
        }

        btnRoom.setOnClickListener{
            readTest(roomRepo, createResultDrawer(pbRoom, tvRoom))
        }

        btnSql.setOnClickListener {
            readTest(sqliteRepo, createResultDrawer(pbSql, tvSql))
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

        val dataSource = GitHelper.getUsers()

        disposables.add(
            Completable.mergeArray(
                realmRepo.loadUsers(dataSource, recordsMultiplier),
                roomRepo.loadUsers(dataSource, recordsMultiplier),
                sqliteRepo.loadUsers(dataSource, recordsMultiplier))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{animator.displayedChild = layerContent}
        )

        dataSource.connect()
    }

    private fun readTest(repo : IRepo, resultHandler : (Pair<Int, Long>) -> Unit ) {

        val timeStart : Long = System.currentTimeMillis()
        var records = 0

        disposables.add(
            // Чтение из базы в массив
            repo.getUsers()
                .subscribeOn(Schedulers.computation())
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

//    private val setupWatcher = SingleObserver<List<>>
}