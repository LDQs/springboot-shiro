package com.ldq.mapper;

import com.ldq.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByName(String username);

    User findByUserId(String userId);

    Integer updatePwdByUserId(@Param("userId")String userId,@Param("newPwd")String newPwd);

    Integer updateByUserId(@Param("userName")String userName,@Param("password")String password,@Param("userId")String userId,@Param("identity")String identity,@Param("status")String status);

    Integer deleteByUserId(String userId);

    List<User> getAllUsers(@Param("rowstart")Integer rowstart, @Param("pagesize")Integer pagesize);

    Integer getAllUsersNum();

    Integer addUser(@Param("userName")String userName,@Param("password")String password,@Param("userId")String userId,@Param("identity")String identity,@Param("status")String status);
}
