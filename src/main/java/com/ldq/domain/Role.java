package com.ldq.domain;

import java.util.List;

public class Role {
    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private List<String> roleRight;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public List<String> getRoleRight() {
        return roleRight;
    }

    public void setRoleRight(List<String> roleRight) {
        this.roleRight = roleRight;
    }
}
