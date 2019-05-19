package cn.fchen.myplugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * @Classname QueryLimitPlugin
 * @Description 查询限制条件
 * @Date 2019/5/19 15:38
 * @Author by Chen
 */
@Intercepts({@Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class})})
public class QueryLimitPlugin implements Interceptor {
    // 默认限制查询返回行数
    private int limit;

    private String dbType;

    //限制表中间别名，避免表名重复
    private static final String LMT_TABLE_NAME = "limit_Table_Name_xxx";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取被拦截的对象
        StatementHandler target = (StatementHandler)invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(target);
        //分离代理对象从而形成多次代理，通过两次循环最原始的代理类
        while (metaObject.hasGetter("h")){
            Object o = metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(o);
        }
        //分离最后一个代理对象的目标类
        while (metaObject.hasGetter("target")){
            Object targer = metaObject.getValue("targer");
            metaObject = SystemMetaObject.forObject(targer);
        }
        //取出即将执行的sql
        String sql = (String)metaObject.getValue("delegate.bound.sql");
        String limitSql;
        //判断是不是mysql数据库 且 sql 有没有被插件重新写过
        if("mysql".equals(this.dbType) && sql.indexOf(LMT_TABLE_NAME) == -1){
            //去掉前后空格
            sql = sql.trim();
            //将参数写入sql
            limitSql = "select * from ("+ sql +")" + LMT_TABLE_NAME + " limit " + limit;
            //重写要执行的sql
            metaObject.setValue("delegate.boundSql.sql",limitSql);
        }
        //调用原来对象的方法，进入责任链的下一层级
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
        String strLimit = properties.getProperty("limit", "50");
        this.limit = Integer.parseInt(strLimit);
        //这里读取数据的设置类型
        this.dbType = (String)properties.getProperty("dbType","mysql");

    }
}
