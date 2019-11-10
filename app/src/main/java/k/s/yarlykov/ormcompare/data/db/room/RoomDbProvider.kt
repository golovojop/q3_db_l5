package k.s.yarlykov.ormcompare.data.db.room

import androidx.room.*
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRoom

@Dao
interface RoomDbProvider : DbProvider<UserRoom, List<User>> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(us : Iterable<UserRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(u: UserRoom)

    @Update
    override fun update(u: UserRoom)

    @Delete
    override fun delete(u: UserRoom)

    @Query("select * from UserRoom")
    override fun select(): List<User>

    @Query("delete from UserRoom")
    override fun clear()
}