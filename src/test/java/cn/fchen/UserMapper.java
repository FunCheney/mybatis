package cn.fchen;

import java.util.List;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2019/2/18 18:01
 * @Author by Fchen
 */
public interface UserMapper {
    User getUser(Integer id);

    List<User> getUserList(List<Integer> ids);
}
