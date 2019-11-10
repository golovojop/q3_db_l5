package k.s.yarlykov.ormcompare.data.db.orm

import io.realm.Realm
import io.realm.RealmResults
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm
import k.s.yarlykov.ormcompare.domain.toUser

class RealmDbProvider : DbProvider<UserRealm, List<User>> {

    override fun insert(us: Iterable<UserRealm>) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.copyToRealm(us)
            realm.commitTransaction()
        }
    }

    override fun insert(u: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.insertOrUpdate(u)
            realm.commitTransaction()
        }
    }

    override fun update(u: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.copyToRealmOrUpdate(u)
            }
        }
    }

    override fun delete(u: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.where(UserRealm::class.java).contains("login", u.login).findAll().deleteFirstFromRealm()
            }
        }
    }

    override fun select(): List<User> {

        return Realm.getDefaultInstance().use { realm ->
            val results: RealmResults<UserRealm> =
                realm
                    .where(UserRealm::class.java)
                    .findAll()
            realm.copyFromRealm(results)
                .asSequence()
                .map { userRealm ->
                    userRealm.toUser()
                }
                .toList()
        }
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}