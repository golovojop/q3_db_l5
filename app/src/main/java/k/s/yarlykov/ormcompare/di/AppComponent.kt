package k.s.yarlykov.ormcompare.di

import dagger.Component
import k.s.yarlykov.ormcompare.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules=[AppModule::class/*, NetworkModule::class*/])
interface AppComponent {
    fun inject(activity : MainActivity)
}