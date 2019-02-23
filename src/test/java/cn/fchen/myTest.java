package cn.fchen;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Classname myTest
 * @Description test类
 * @Date 2019/2/18 17:37
 * @Author by Fchen
 */
public class myTest {

    private SqlSessionFactory sqlSessionFactory = null;

    @Before
    public void init() throws IOException {
        // 1.加载核心配置文件（sqlMapConfig.xml）
        // resource参数：指定配置文件的位置
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config-local.xml");

        // 2.读取配置文件内容
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // sqlSessionFactory对象：mybatis框架的核心对象，通常一个应用中只需要一个（单例设计模式），是线程安全
        sqlSessionFactory = builder.build(inputStream);
    }

    // 测试根据用户id查询用户
    @Test
    public void queryUserByIdTest() throws IOException{

        SqlSession session = sqlSessionFactory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.getUser(1);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        System.out.println(user);

        // 5.释放资源
        sqlSession.close();
    }
}
