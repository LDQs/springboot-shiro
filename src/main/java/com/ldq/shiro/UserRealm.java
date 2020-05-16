package com.ldq.shiro;

import com.ldq.dao.RoleServiceImpl;
import com.ldq.dao.UserServiceImpl;
import com.ldq.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private RoleServiceImpl roleService;

    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");

        // 给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 到数据库查询当前登录用户的授权字符串
        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        // 查找当前用户的角色id
        Integer roleId = roleService.findByRoleName(user.getStatus());
        // 查找当前用户角色对应的权限
        List<String> rights = roleService.getRightCodeByRoleId(roleId);
//        System.out.println(rights.toString());
        // 添加权限
        info.addStringPermissions(rights);

        System.out.println("执行授权逻辑结束");

        return info;
    }

    @Autowired
    private UserServiceImpl userService;

    /**
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        // 编写shiro判断逻辑，判断用户名和密码
        // 1.判断用户名
//        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//        String username = authenticationToken.getPrincipal().toString();
//        System.out.println("username"+username);
//        User user = this.userService.findByName(username);
//        System.out.println("user"+user);

        String userId = authenticationToken.getPrincipal().toString();
        User user = this.userService.findByUserId(userId);
        if (user == null) {
            // 用户名不存在
            return null; // shiro底层会抛出UnknownAccountException异常
        }
        System.out.println("执行认证逻辑结束");
        // 2.判断密码
        //盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserName());
        return new SimpleAuthenticationInfo(user,user.getPassword(),credentialsSalt,getName());
    }
}
