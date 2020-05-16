package com.ldq.dao;

import com.ldq.domain.Role;
import com.ldq.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleMapper {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public List<String> getRightNameByRoleId(Integer roleId) {
        return roleMapper.getRightNameByRoleId(roleId);
    }

    @Override
    public List<String> getRightCodeByRoleId(Integer roleId) {
        return roleMapper.getRightCodeByRoleId(roleId);
    }

    @Override
    public Role findByRoleId(Integer roleId) {
        return roleMapper.findByRoleId(roleId);
    }

    @Override
    public Integer findByRoleName(String roleName) {
        return roleMapper.findByRoleName(roleName);
    }

    @Override
    public Integer updateByRoleId(Integer roleId, String roleName, String roleDesc) {
        return roleMapper.updateByRoleId(roleId,roleName,roleDesc);
    }

    @Override
    public Integer deleteAllRights(Integer roleId) {
        return roleMapper.deleteAllRights(roleId);
    }

    @Override
    public Integer updateRightByRoleId(Integer roleId, Integer rightId) {
        return roleMapper.updateRightByRoleId(roleId,rightId);
    }

}
