package com.ldq.controller;

import com.ldq.dao.UserServiceImpl;
import com.ldq.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.dao.DataAccessException;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 获取全部用户信息
     * @param request
     * @return
     */
    @RequestMapping("/users/getAllUsers")
    @ResponseBody
    @CrossOrigin
    public Map getAllUsers(HttpServletRequest request) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();
        Integer pagenum = Integer.parseInt(request.getParameter("pagenum"));
        Integer pagesize = Integer.parseInt(request.getParameter("pagesize"));
        String query = request.getParameter("query");

        // 看是查询全部用户还是指定用户
        if (!query.equals("")) {
            User findUserByName = userService.findByName(query);
            User findUserById = userService.findByUserId(query);
            List<User> user = new ArrayList<>();
            if (findUserById != null) {
                user.add(findUserById);
                res.put("users",user);
                res.put("total",1);
            } else {
                user.add(findUserByName);
                res.put("users",user);
                res.put("total",1);
            }
        } else {
            List<User> users = userService.getAllUsers(pagenum,pagesize);
            Integer total = userService.getAllUsersNum();
            res.put("users",users);
            res.put("total",total);
        }

        return res;
    }

    /**
     * 添加用户
     * @param params
     * @return
     */
    @RequestMapping(value="/users/addUser",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map addUser(@RequestBody Map params) {
        String userName = params.get("userName").toString();
        String password = params.get("password").toString();
        String userId = params.get("userId").toString();
        String identity = params.get("identity").toString();
        String status = params.get("status").toString();

        // 对用户的密码进行MD5加密存储
        Object salt = ByteSource.Util.bytes(userName);
        // 参数含义：加密算法，加密的对象，盐值，加密的次数
        SimpleHash simpleHash=new SimpleHash("MD5", password, salt, 1);

        boolean isSuccess = false;

        // 先判断用户是否存在
        User user = userService.findByUserId(userId);

        if (user != null) {
            isSuccess = false;
        } else {
            if (userService.addUser(userName, simpleHash.toString(), userId, identity,status) == 1) {
                isSuccess = true;
            }
        }

        Map<String,Object> res = new LinkedHashMap<String, Object>();

        res.put("status",isSuccess);
        return res;
    }

    /**
     * 通过学号或者工号查询用户
     * @param userId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @GetMapping("/users/{userId}")
    public Map findByUserId(@PathVariable("userId") String userId) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();
        User user = userService.findByUserId(userId);
        if (user != null) {
            res.put("data",user);
            res.put("status",true);
        } else {
            res.put("data","");
            res.put("status",false);
        }
        return res;
    }

    /**
     * 修改用户信息
     * @param userId
     * @param params
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/users/{userId}")
    public Map updateByUserId(@PathVariable("userId") String userId,@RequestBody Map params) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();

        String userName = params.get("userName").toString();
        String password = params.get("password").toString();
        String identity = params.get("identity").toString();
        String status = params.get("status").toString();

        // 通过受影响的行数来判断是否更新成功
        Integer updateLine = userService.updateByUserId(userName, password, userId, identity, status);

        if (updateLine == 1) {
            res.put("status",true);
        } else {
            res.put("status",false);
        }
        return res;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/users/{userId}")
    public Map deleteByUserId(@PathVariable("userId") String userId) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();

        // 通过受影响的行数来判断是否更新成功
        Integer deleteLine = userService.deleteByUserId(userId);

        if (deleteLine == 1) {
            res.put("status",true);
        } else {
            res.put("status",false);
        }
        return res;
    }


    /**
     * 用户修改密码
     * @param params
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value="/users/changePwd",method = RequestMethod.POST)
    public Map changePwd(@RequestBody Map params) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();

        String userId = params.get("userId").toString();
        String idCard = params.get("idCard").toString();
        String newPassword = params.get("newPassword").toString();

        User user = userService.findByUserId(userId);

        // 用户存在
        if (user != null) {
            // 输入的身份证正确
            if (user.getIdentity().equals(idCard)) {
                // 对新密码进行MD5加密存储
                Object salt = ByteSource.Util.bytes(user.getUserName());
                // 参数含义：加密算法，加密的对象，盐值，加密的次数
                SimpleHash simpleHash=new SimpleHash("MD5", newPassword, salt, 1);
                userService.updatePwdByUserId(userId,simpleHash.toString());
                res.put("status",true);
                return res;
            }
            res.put("status",false);
            res.put("msg","身份证输入有误");
            return res;
        } else {
            res.put("status",false);
            res.put("msg","用户不存在");
            return res;
        }
    }
}
