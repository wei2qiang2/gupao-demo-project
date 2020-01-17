package com.gupaoedu.entity.ext;

import com.gupaoedu.entity.Blog;

import java.util.List;

public class AuthorAndBlog {

    private Integer authorId;
    private String AuthorName;

    private List<BlogAndComment> blogList;

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public List<BlogAndComment> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<BlogAndComment> blogList) {
        this.blogList = blogList;
    }

    @Override
    public String toString() {
        return "AuthorAndBlog{" +
                "authorId=" + authorId +
                ", AuthorName='" + AuthorName + '\'' +
                ", blogList=" + blogList +
                '}';
    }
}
