package com.web.forum.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.forum.DAO.RoleDAO;
import com.web.forum.Security.Error.CustomErrors.ConflictError;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

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
