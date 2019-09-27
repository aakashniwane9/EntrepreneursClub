package com.club.business.Adapters;

import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

public class ForumList {

    String user,post,time,key,imgURL;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ForumList(String user, String post, String time, String key,String imgurl) {
        this.user = user;
        this.post = post;
        this.time = time;
        this.key=key;
        this.imgURL=imgurl;
    }

    public String getImgURL() { return imgURL; }

    public String getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public String getTime() {
        return time;
    }
}
