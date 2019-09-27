package com.club.business.Firebase;

import java.util.HashMap;
import java.util.Map;

public class NewPost {

    String forumpost;
    String timestamp,subject,imageURL,name,uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getForumpost() {
        return forumpost;
    }

    public void setForumpost(String forumpost) {
        this.forumpost = forumpost;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public String getSubject() {
        return subject;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ForumPost",forumpost);
        result.put("ImageURL",imageURL);
        result.put("TimeStamp",timestamp);
        result.put("Subject",subject);
        return result;
    }
}
