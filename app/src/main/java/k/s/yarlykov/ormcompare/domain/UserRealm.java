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

//    public UserRealm(int id, String login, String avatarUrl, String reposUrl) {
//        this.id = id;
//        this.login = login;
//        this.avatarUrl = avatarUrl;
//        this.reposUrl = reposUrl;
//    }

    public UserRealm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }
}
