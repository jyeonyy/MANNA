package org.ssutown.manna.Data;

import java.io.Serializable;

/**
 * Created by JeongHun on 16. 7. 15..
 */
public class KakaoProfile implements Serializable {

    private long id;
    private String nickName;
    private String thumbNailUrl;
    private String ProfilUrl;

    public KakaoProfile(long id, String nickName, String profilUrl, String thumbNailUrl) {
        this.id = id;
        this.nickName = nickName;
        ProfilUrl = profilUrl;
        this.thumbNailUrl = thumbNailUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilUrl() {
        return ProfilUrl;
    }

    public void setProfilUrl(String profilUrl) {
        ProfilUrl = profilUrl;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    @Override
    public String toString() {
        return "KakaoProfile{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", thumbNailUrl='" + thumbNailUrl + '\'' +
                ", ProfilUrl='" + ProfilUrl + '\'' +
                '}';
    }
}
