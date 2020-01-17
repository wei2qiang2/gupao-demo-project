package com.gupaoedu.dao;

import com.gupaoedu.entity.Blog;
import com.gupaoedu.entity.ext.AuthorAndBlog;
import com.gupaoedu.entity.ext.BlogAndAuthor;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer bid);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Integer bid);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    Blog selectBlogByBean(Blog blog);

    List<Blog> selectBlogList(RowBounds rb);

    List<BlogAndAuthor> selectBlogAndAuthorOneToOne();

    List<AuthorAndBlog> selectAuthorWithBlogAndComment();
}