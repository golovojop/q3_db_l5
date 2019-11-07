package k.s.yarlykov.ormcompare.data.db.orm

import io.realm.Realm
import io.realm.RealmResults
import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm
import k.s.yarlykov.ormcompare.domain.toUser

class RealmDbProvider : DbProvider<UserRealm, List<User>> {

    override fun insert(u: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()

//            val user = realm.copyToRealmOrUpdate(u)
//            val user = realm.copyToRealm(u)
//            realm.insertOrUpdate(user)
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
}