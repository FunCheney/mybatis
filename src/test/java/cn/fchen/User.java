package cn.fchen;


import lombok.Data;
import lombok.ToString;

/**
 * @Classname User
 * @Description 对应的用户类
 * @Date 2019/2/18 17:46
 * @Author by Fchen
 */
@Data
@ToString
public class User {
    private int id;
    private String name;
    private int age;
}
