package com.ldq.controller;

import com.ldq.dao.RoleServiceImpl;
import com.ldq.dao.UserServiceImpl;
import com.ldq.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins="http://localhost:8080")
public class LoginController {
    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UserServiceImpl userService;

    /**
     * 登录逻辑处理
     * @param params
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody Map params,HttpServletRequest request) {
        String userId = params.get("userId").toString();
        String password = params.get("password").toString();
        String rememberMe = params.get("rememberMe").toString();
        Boolean remMe = false;
        if (rememberMe.equals("true")) {
            remMe = true;
        }

        /**
        * 使用Shiro编写认证操作
        */
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(userId,password);

        // 3.执行登录方法
        // 4.将subject存入session
        Map<String, Object> map = new HashMap<>(2);
        try {
            subject.login(token);
            // 到数据库查询当前登录用户的授权字符串
            // 获取当前登录用户
            User user = (User)subject.getPrincipal();
            // 查找当前用户的角色id
            Integer roleId = roleService.findByRoleName(user.getStatus());
            // 查找当前用户角色对应的权限
            List<String> rights = roleService.getRightCodeByRoleId(roleId);
            // 将token、rights存入session
            List<Object> userMsg = new ArrayList<>();
            userMsg.add(token);
            userMsg.add(rights);
            request.getSession().setAttribute("user",userMsg);

            // 获取用户的所有信息
            User userInfo = userService.findByUserId(userId);

            // 登录成功
            map.put("status",200);
            map.put("info",userInfo);
            map.put("msg","登录成功");
            return map;
        } catch (UnknownAccountException e) {
            // 登录失败：用户名不存在
            map.put("status",500);
            map.put("msg","用户名不存在");
            return map;
        } catch (IncorrectCredentialsException e) {
            // 登录失败：密码错误
            map.put("status",500);
            map.put("msg","密码错误");
            return map;
        }
    }
}
