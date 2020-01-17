package com.gupao;

import com.gupaoedu.dao.BlogMapper;
import com.gupaoedu.dao.MenuMapper;
import com.gupaoedu.entity.Blog;
import com.gupaoedu.entity.Menu;
import com.gupaoedu.entity.ext.AuthorAndBlog;
import com.gupaoedu.entity.ext.BlogAndAuthor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {

    private BlogMapper blogMapper = null;
    private String name = "mybatis-config.xml";
    private SqlSession sqlSession = null;

    @Before
    public void before() throws IOException {
        sqlSession = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(name)).openSession();

        blogMapper = sqlSession.getMapper(BlogMapper.class);
    }

    @After
    public void after(){
        if (sqlSession != null){
            sqlSession.close();
        }
    }

    private void commit(){
        if (sqlSession == null){
            throw new RuntimeException("sqlSession is null, commit failure");
        }
        sqlSession.commit();
    }
    /**
     * 传统的API方式
     */
    @Test
    public void testOne() throws IOException {
        String configFile = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(configFile);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Blog blog = sqlSession.selectOne("selectByPrimaryKey", 1);
        System.out.println(blog.toString());
        sqlSession.close();
    }

    /**
     * 通过sqlSession.getMapper(Mapper.class)接口方式
     */
    @Test
    public void testTwo() throws IOException {
        String file = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(file);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog = mapper.selectByPrimaryKey(1);
        System.out.println(blog.toString());
    }

    @Test
    public void testInsert() throws IOException {
        String fileName = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(fileName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setAuthorId(20);
        blog.setBid(21);
        blog.setName("new Blog");
        int insert = mapper.insert(blog);

        sqlSession.commit();
        sqlSession.close();
        System.out.println("insert num: " + insert);
    }

    /**
     * 测试#和$的差别
     */
    @Test
    public void testThree() throws IOException {
        BlogMapper mapper = new SqlSessionFactoryBuilder()
                .build(Resources
                        .getResourceAsStream("mybatis-config.xml"))
                .openSession()
                .getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setName("new Blog");
        Blog blog1 = mapper.selectBlogByBean(blog);
        System.out.println(blog1.toString());
    }

    /**
     * 逻辑分页
     * 内存分页
     */
    @Test
    public void testLogicPage(){
        int start = 0, pageSize = 1;
        RowBounds rb = new RowBounds(start, pageSize);
        List<Blog> blogs = blogMapper.selectBlogList(rb);
        for (int i = 0; i < blogs.size() ; i++) {
            System.out.println(blogs.get(i).toString());
        }
    }

    @Test
    public void testOneToOne(){
        List<BlogAndAuthor> blogAndAuthors = blogMapper.selectBlogAndAuthorOneToOne();
        for (int i = 0; i < blogAndAuthors.size() ; i++) {
            System.out.println(blogAndAuthors.get(i).toString());
        }
    }

    /**
     * 递归关联查询
     * @throws IOException
     */
    @Test
    public void testRecuri() throws IOException {
        String file = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(file);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MenuMapper mapper = sqlSession.getMapper(MenuMapper.class);
        List<Menu> menus = mapper.selectRecursionRes(0);
        for (int i = 0; i < menus.size(); i++) {
            System.out.println(menus.size());
            System.out.println(menus.get(i).toString());
        }
    }

    @Test
    public void testManyToMany(){
        List<AuthorAndBlog> authorAndBlogs = blogMapper.selectAuthorWithBlogAndComment();
        for (int a = 0; a <authorAndBlogs.size() ; a++) {
            System.out.println(authorAndBlogs.get(a).toString());
        }
    }
}

