package com.greenlucky.utils;

/**
 * Didines possible roles
 * Created by Mr Lam on 6/11/2016.
 */
public enum RolesEnum {
    BASIC(1, "ROLE_BASIC"),
    PRO(2, "ROLE_PRO"),
    ADMIN(3,"ROLE_ADMIN");

    private int id;

    private String roleName;

    RolesEnum(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}
