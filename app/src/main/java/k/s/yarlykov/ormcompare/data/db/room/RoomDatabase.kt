package k.s.yarlykov.ormcompare.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import k.s.yarlykov.ormcompare.domain.UserRoom

@Database(entities = arrayOf(UserRoom::class), version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun roomDbProvider() : RoomDbProvider
}