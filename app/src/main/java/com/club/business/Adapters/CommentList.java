package com.club.business.Adapters;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CommentList {

    //ImageView comment_profile_picture;
    String comment_timestamp,comment_username,comment_usercomment;

    public CommentList(String comment_timestamp, String comment_username, String comment_usercomment) {
        this.comment_timestamp = comment_timestamp;
        this.comment_username = comment_username;
        this.comment_usercomment = comment_usercomment;
    }

    public String getComment_timestamp() {
        return comment_timestamp;
    }

    public String getComment_username() {
        return comment_username;
    }

    public String getComment_usercomment() {
        return comment_usercomment;
    }
}
