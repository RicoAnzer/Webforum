package com.web.forum.Repository.Interfaces;

import com.web.forum.Entity.Role;

public interface IRoleRepository {

    //Create and save new Role
    public String save(String roleName);

    //Find specific Role based on ID
    public Role find(int ID);

    //Delete existing Role
    public String remove(String roleName);
}
