package com.gupaoedu.entity.ext;

import com.gupaoedu.entity.Comment;

import java.util.List;

public class BlogAndComment {

    private  Integer bId;

    private String name;

    private Integer authorId;

    private List<Comment> commentList;

    public Integer getBid() {
        return bId;
    }

    public void setBid(Integer bid) {
        this.bId = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "BlogAndComment{" +
                "bid=" + bId +
                ", name='" + name + '\'' +
                ", authorId=" + authorId +
                ", commentList=" + commentList +
                '}';
    }
}
