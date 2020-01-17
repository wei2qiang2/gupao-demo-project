package com.gupaoedu.entity;

public class Comment {
    private Integer commentId;

    private String content;

    private Integer bId;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getBid() {
        return bId;
    }

    public void setBid(Integer bid) {
        this.bId = bid;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", content='" + content + '\'' +
                ", bid=" + bId +
                '}';
    }
}