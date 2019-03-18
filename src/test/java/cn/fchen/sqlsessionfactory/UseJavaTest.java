package cn.fchen.sqlsessionfactory;

import cn.fchen.User;
import cn.fchen.UserMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * @Classname UseJavaTest
 * @Description 通过非xml方式获取  SqlSessionFactory
 * @Date 2019/3/14 19:03
 * @Author by Fchen
 */
public class UseJavaTest {

    @Test
    public void getSqlSessionFactory() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        //构建数据库事务
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //创建数据库运行环境
        Environment jdbc = new Environment("JDBC", transactionFactory, dataSource);
        //构建configuration对象
        Configuration configuration = new Configuration(jdbc);
        //注册MyBatis的上下文别名
        configuration.getTypeAliasRegistry().registerAlias("user", User.class);
        //加入一个映射器
        configuration.addMapper(UserMapper.class);
        //构建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        System.out.println(sqlSessionFactory);
    }
}
