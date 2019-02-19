package cn.fchen.test;

import cn.fchen.test.dao.UserMapper;
import cn.fchen.test.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * @Classname myTest
 * @Description test类
 * @Date 2019/2/18 17:37
 * @Author by Fchen
 */
public class myTest {
    protected SqlSessionFactory sqlSessionFactory;

    @Before
    public void init(){
        try {
            String resource = "conf/mybatis-config-local.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            System.out.println("sqlSessionFactory初始化-->"+sqlSessionFactory);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void test(){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.getUser(1);
            System.out.println(user.getName());
        } finally {
            session.close();
        }
    }
    @Test
    public void testLombok(){
        User user = new User();
        user.setName("熊王");
        user.setId(1);
        System.out.println(user.toString());
    }
}
