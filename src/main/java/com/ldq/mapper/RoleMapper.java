package com.ldq.mapper;

import com.ldq.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> getAllRoles();

    List<String> getRightNameByRoleId(Integer roleId);

    List<String> getRightCodeByRoleId(Integer roleId);

    Role findByRoleId(Integer roleId);

    Integer findByRoleName(String roleName);

    Integer updateByRoleId(@Param("roleId")Integer roleId, @Param("roleName")String roleName, @Param("roleDesc")String roleDesc);

    Integer deleteAllRights(Integer roleId);

    Integer updateRightByRoleId(@Param("roleId")Integer roleId,@Param("rightId")Integer rightId);
}
