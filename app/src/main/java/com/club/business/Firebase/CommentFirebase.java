package com.club.business.Firebase;

import java.util.HashMap;

public class CommentFirebase {
    String timestamp,name,comment;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public HashMap<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Comment",comment);
        result.put("TimeStamp",timestamp);
        result.put("Name",name);
        return result;
    }
}
