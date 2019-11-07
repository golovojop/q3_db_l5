package k.s.yarlykov.ormcompare.repository.sqlite

import k.s.yarlykov.ormcompare.data.db.DbProvider
import k.s.yarlykov.ormcompare.domain.User
import k.s.yarlykov.ormcompare.domain.UserRealm

class SqliteRepo(private val dbRealm: DbProvider<UserRealm, List<User>>) : ISqliteRepo {

}