package k.s.yarlykov.ormcompare.di.component

import dagger.Component
import k.s.yarlykov.ormcompare.MainActivity
import k.s.yarlykov.ormcompare.di.module.AppModule
import k.s.yarlykov.ormcompare.di.module.OrmRealmModule
import javax.inject.Singleton

@Singleton
@Component(modules=[AppModule::class, OrmRealmModule::class/*, NetworkModule::class*/])
interface AppComponent {
    fun inject(activity : MainActivity)
}