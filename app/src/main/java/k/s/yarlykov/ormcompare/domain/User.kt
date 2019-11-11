package k.s.yarlykov.ormcompare.domain

data class User(val id: Int, val login : String, val avatarUrl: String, var reposUrl: String) : Comparable<User> {

    override fun compareTo(other: User): Int {
        return this.id - other.id
    }
}
