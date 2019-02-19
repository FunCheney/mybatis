package cn.fchen;

import org.apache.ibatis.domain.blog.mappers.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @Classname myTest
 * @Description test类
 * @Date 2019/2/18 17:37
 * @Author by Fchen
 */
public class myTest {

    public static void main(String[] args) {
        try {
            String resource = "mybatis-config-local.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            System.out.println("sqlSessionFactory初始化-->"+sqlSessionFactory);
            SqlSession session = sqlSessionFactory.openSession();
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.getUser(1);
            System.out.println(user);
            session.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
