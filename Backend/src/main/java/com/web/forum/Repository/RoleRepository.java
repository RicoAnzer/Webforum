package com.web.forum.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.RoleDAO;
import com.web.forum.Entity.Role;
import com.web.forum.Repository.Interfaces.IRoleRepository;

@Repository
public class RoleRepository implements IRoleRepository {

    @Autowired
    private RoleDAO roleDAO;

    //Create and save new Role
    @Override
    public String save(String roleName) {
        return roleDAO.create(roleName);
    }

    //Find specific Role based on ID
    @Override
    public Role find(int ID) {
        return roleDAO.read(ID);
    }

    //Delete existing Role
    @Override
    public String remove(String roleName) {
        return roleDAO.delete(roleName);
    }
}
