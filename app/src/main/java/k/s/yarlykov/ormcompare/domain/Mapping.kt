package k.s.yarlykov.ormcompare.domain

import k.s.yarlykov.ormcompare.rotate

fun UserGit.toUserRealm(): UserRealm {
    val userRealm =  UserRealm()

    userRealm.setId(this.id)
    userRealm.setLogin(this.login)
    userRealm.setAvatarUrl(this.avatarUrl)
    userRealm.setReposUrl(this.reposUrl)

    return userRealm
}

fun UserGit.toUserSqlite(): UserSqlite {
    val userSqlite =  UserSqlite()

    userSqlite.setId(this.id)
    userSqlite.setLogin(this.login)
    userSqlite.setAvatarUrl(this.avatarUrl)
    userSqlite.setReposUrl(this.reposUrl)

    return userSqlite
}

fun UserGit.toUserRoom(): UserRoom {
    val userRoom =  UserRoom()

    userRoom.id = this.id
    userRoom.login = this.login
    userRoom.avatarUrl =this.avatarUrl
    userRoom.reposUrl = this.reposUrl

    return userRoom
}

fun UserGit.toUser(): User = User(
    this.id,
    this.login,
    this.avatarUrl,
    this.reposUrl
)

fun UserRealm.toUser() : User = User(
    this.id,
    this.login,
    this.avatarUrl,
    this.reposUrl
)

fun UserRoom.toUser() : User = User(
    this.id,
    this.login,
    this.avatarUrl,
    this.reposUrl
)

/**
 * @gitUsers - на вход список юзеров из гита
 * @List<R> - на выходе увеличенный в multiplier раз список элементов типа R
 */
fun <R> multiplyMap(gitUsers : List<UserGit>, multiplier: Int, mapper : UserGit.() -> R) : List<R> {

    val li = mutableListOf<R>()

    val step = gitUsers.map { it.id }.max()!!

    (0 until multiplier).forEach { i ->
        gitUsers.forEach { user ->
            UserGit().apply {
                id = user.id + (i * step)
                login = "${user.login.rotate(1)}_$id"
                avatarUrl = user.avatarUrl
                reposUrl = user.reposUrl

                li.add(mapper())
            }
        }
    }

    return li
}
