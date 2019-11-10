package k.s.yarlykov.ormcompare.data.db.room

import androidx.room.*
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRoom

@Dao
interface UserDao : DbProvider<UserRoom, List<User>> {

    @Insert
    override fun insert(us : Iterable<UserRoom>)

    @Insert
    override fun insert(u: UserRoom)

    @Update
    override fun update(u: UserRoom)

    @Delete
    override fun delete(u: UserRoom)

    @Query("select * from UserRoom")
    override fun select(): List<User>

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}