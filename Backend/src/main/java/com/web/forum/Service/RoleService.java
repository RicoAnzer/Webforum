package com.web.forum.Service;

import org.springframework.stereotype.Service;

import com.web.forum.DAO.RoleDAO;
import com.web.forum.Security.Error.CustomErrors.ConflictError;

//RoleService to manage database traffic of Roles (create, delete)
@Service
public class RoleService {

    private final RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public String createRole(String roleName){
        if (roleDAO.readByName(roleName) != null) {
            throw new ConflictError("Role with this name already exists");
        }
        return roleDAO.create(roleName);
    }

     public String deleteRole(String roleName){
        return roleDAO.delete(roleName);
    }
}
