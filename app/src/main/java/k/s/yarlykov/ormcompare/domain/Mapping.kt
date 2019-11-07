package k.s.yarlykov.ormcompare.domain

fun UserGit.toUserRealm(): UserRealm {
    val userRealm =  UserRealm()

    userRealm.setId(this.id)
    userRealm.setLogin(this.login)
    userRealm.setAvatarUrl(this.avatarUrl)
    userRealm.setReposUrl(this.reposUrl)

    return userRealm
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
