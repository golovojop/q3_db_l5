package k.s.yarlykov.ormcompare.data.orm

import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm
import k.s.yarlykov.ormcompare.domain.toUser

class RealmDbProvider : DbProvider<UserRealm, List<User>> {

    override fun insert(t: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.insertOrUpdate(t)
            realm.commitTransaction()
        }
    }

    override fun update(t: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.copyToRealmOrUpdate(t)
            }
        }
    }

    override fun delete(t: UserRealm) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.where(UserRealm::class.java).contains("..", t.login).findAll().deleteFirstFromRealm()
            }
        }
    }

    override fun select(): List<User> {

        return Realm.getDefaultInstance().use { realm ->
            val results: RealmResults<UserRealm> =
                realm
                    .where(UserRealm::class.java)
                    .contains("login", "tak", Case.SENSITIVE )
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