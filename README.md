##MyBatis 源码阅读
### 1.环境搭建
#### 1.1准备条件
     IDEA
     gradle
       1.从gitHub克隆代码 https://github.com/mybatis/mybatis-3.git
       2.到clone下来的代码对应的pom.xml文件所在的地方，执行 gradle init --type pom 命令，先保证本机安装了gradle 2.0以上的版本
       3.删除原先的pom.xml文件
       4.在build.gradle 中加入 compile group: 'mysql', name: 'mysql-connector-java', version:'5.1.32'
                                        
                                        