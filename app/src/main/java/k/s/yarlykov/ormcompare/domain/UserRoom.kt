package k.s.yarlykov.ormcompare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserRoom {

    @PrimaryKey
    var id: Int = 0

    var login: String = ""
    var avatarUrl: String = ""
    var reposUrl: String = ""
}