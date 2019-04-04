package cn.fchen;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname myTest
 * @Description test类
 * @Date 2019/2/18 17:37
 * @Author by Fchen
 */
public class myTest {

    private SqlSessionFactory sqlSessionFactory = null;
    private SqlSession session = null;

    @Before
    public void init() throws IOException {
        // 1.加载核心配置文件（mybatis-config-local.xml）
        // resource参数：指定配置文件的位置
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config-local.xml");

        // 2.创建SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // sqlSessionFactory对象：mybatis框架的核心对象，通常一个应用中只需要一个（单例设计模式），是线程安全
        sqlSessionFactory = builder.build(inputStream);
    }

    // 测试根据用户id查询用户
    @Test
    public void queryUserByIdTest() throws IOException{
        //3.获取SqlSession对象
        session = sqlSessionFactory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        //4.执行sql语句
        User user = mapper.getUser(1);
        System.out.println(user);
    }

    @Test
    public void queryUserBySession() throws IOException{
        //3.获取SqlSession对象
        session = sqlSessionFactory.openSession();
        User user = (User) session.selectOne("cn.fchen.UserMapper.getUser", 1);
        System.out.println(user);
    }

    @Test
    public void queryUserBySessionList() throws IOException{
        //3.获取SqlSession对象
        session = sqlSessionFactory.openSession();
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        User user = (User) session.selectList("cn.fchen.UserMapper.getUserList", ids);
        System.out.println(user);
    }
    @After
    public void close(){
        if(session != null){
            // 5.释放资源
            session.close();
        }
    }
}
