package k.s.yarlykov.ormcompare.domain;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class UserRealm implements RealmModel {
    @PrimaryKey
    public int id;

    public String login;
    public String avatarUrl;
    public String reposUrl;

    public UserRealm(int id, String login, String avatarUrl, String reposUrl) {
        this.id = id;
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.reposUrl = reposUrl;
    }

    public UserRealm() {
    }
}
