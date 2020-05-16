package com.ldq.controller;

import com.ldq.dao.RightServiceImpl;
import com.ldq.domain.Role;
import com.ldq.dao.RoleServiceImpl;
import com.ldq.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private RightServiceImpl rightService;

    /**
     * 获取所有角色
     * @return
     */
    @RequestMapping("/roles")
    @ResponseBody
    @CrossOrigin
    public List<Role> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();

        for (int i=0; i<roles.size(); i++) {
            // 查询用户拥有的权限，添加到用户中
            List<String> rights = roleService.getRightNameByRoleId(roles.get(i).getRoleId());
            roles.get(i).setRoleRight(rights);
        }

        return roles;
    }

    /**
     * 通过roleId查找角色
     * @param roleId
     * @return
     */
    @GetMapping("/roles/{roleId}")
    @ResponseBody
    @CrossOrigin
    public Map findByRoleId(@PathVariable("roleId") Integer roleId) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();
        Role role = roleService.findByRoleId(roleId);
        List<String> rights = roleService.getRightNameByRoleId(roleId);
        role.setRoleRight(rights);
        if (role != null) {
            res.put("data",role);
            res.put("status",true);
        } else {
            res.put("data","");
            res.put("status",false);
        }
        return res;
    }

    /**
     * 更新角色信息
     * @param roleId
     * @param params
     * @return
     */
    @PostMapping("/roles/{roleId}")
    @ResponseBody
    @CrossOrigin
    public Map updateByRoleId(@PathVariable("roleId") String roleId,@RequestBody Map params) {
        Map<String,Object> res = new LinkedHashMap<String, Object>();

        String roleName = params.get("roleName").toString();
        String roleDesc = params.get("roleDesc").toString();
        // 删除用户所有权限
        roleService.deleteAllRights(Integer.parseInt(roleId));
        // 判断是否需要添加权限
        if (params.get("roleRight").toString().length() != 0) {
            String[] roleRights = params.get("roleRight").toString().split(",");
            // 然后添加权限
            for (int i=0;i<roleRights.length;i++) {
                Integer rightId = rightService.findByRightName(roleRights[i]);
                Integer updateLine = roleService.updateRightByRoleId(Integer.parseInt(roleId),rightId);
            }
        }

        // 通过受影响的行数来判断是否更新成功
        Integer updateLine = roleService.updateByRoleId(Integer.parseInt(roleId),roleName,roleDesc);

        if (updateLine == 1) {
            res.put("status",true);
        } else {
            res.put("status",false);
        }
        return res;
    }
}
