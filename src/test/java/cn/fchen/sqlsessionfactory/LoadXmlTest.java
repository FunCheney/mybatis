package cn.fchen.sqlsessionfactory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Classname LoadXmlTest
 * @Description 通过xml获取  SqlSessionFactory
 * @Date 2019/3/14 17:03
 * @Author by Fchen
 */

public class LoadXmlTest {

    @Test
    public void getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config-local.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        System.out.println(sqlSessionFactory);
    }
}
