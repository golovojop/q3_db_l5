package k.s.yarlykov.ormcompare.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import k.s.yarlykov.ormcompare.data.db.room.AppRoomDatabase
import k.s.yarlykov.ormcompare.data.db.room.RoomDbProvider
import k.s.yarlykov.ormcompare.repository.IRepo
import k.s.yarlykov.ormcompare.repository.room.RoomRepo
import javax.inject.Named
import javax.inject.Singleton

/**
 * Room хранит базенку в папке data/data/databases/<db_name>
 * Поэтому конфликтует с SQLite. Нужно присваивать другое имя,
 * например "room_$dbName"
 */

@Module
class OrmRoomModule(private val dbName: String) {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppRoomDatabase =
        Room.databaseBuilder(context, AppRoomDatabase::class.java, "room_$dbName").build()

    @Singleton
    @Provides
    fun provideRoomDbProvider(db: AppRoomDatabase): RoomDbProvider =
        db.roomDbProvider()


    @Singleton
    @Named("room_repo")
    @Provides
    fun provideRoomRepository(dbProvider: RoomDbProvider): IRepo =
        RoomRepo(dbProvider)


}