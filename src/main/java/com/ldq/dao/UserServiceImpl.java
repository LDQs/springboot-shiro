package com.ldq.dao;

import com.ldq.domain.User;
import com.ldq.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserMapper {
    // 注入Mapper接口
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByName(String username) {
        return userMapper.findByName(username);
    }

    @Override
    public User findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public Integer updatePwdByUserId(String userId, String newPwd) {
        return userMapper.updatePwdByUserId(userId,newPwd);
    }

    @Override
    public Integer updateByUserId(String userName, String password, String userId, String identity, String status) {
        return userMapper.updateByUserId(userName, password, userId, identity, status);
    }

    @Override
    public Integer deleteByUserId(String userId) {
        return userMapper.deleteByUserId(userId);
    }

    @Override
    public List<User> getAllUsers(Integer pagenum, Integer pagesize) {
        // 查找当前分页的用户
        Integer rowstart = (pagenum-1)*pagesize;
        List<User> users = userMapper.getAllUsers(rowstart,pagesize);

        return users;
    }

    @Override
    public Integer getAllUsersNum() {
        // 查找所有用户的数量
        Integer total = userMapper.getAllUsersNum();
        return total;
    }

    @Override
    public Integer addUser(String userName, String password, String userId, String identity, String status) {
        // 添加用户
        Integer affectLine = userMapper.addUser(userName, password, userId, identity,status);
        return affectLine;
    }

}
