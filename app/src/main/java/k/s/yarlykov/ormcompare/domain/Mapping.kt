package k.s.yarlykov.ormcompare.domain

fun UserGit.toUserRealm(): UserRealm {
    return UserRealm(
        this.id,
        this.login,
        this.avatarUrl,
        this.reposUrl
    )
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
