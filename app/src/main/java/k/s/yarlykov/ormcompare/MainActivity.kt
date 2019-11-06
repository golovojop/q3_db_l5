package k.s.yarlykov.ormcompare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.ormcompare.data.model.github.GitUser
import k.s.yarlykov.ormcompare.data.network.GitHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val d = GitHelper.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::printUsers, ::printError)
    }

    private fun printUsers(list: List<GitUser>) {

        tvInfo.text = list.map {
            "${it.id} ${it.login}"
        }.joinToString("\n")

    }

    private fun printError(t: Throwable) {
        tvInfo.text = t.message.toString()
        logIt(t.message.toString())
    }
}
