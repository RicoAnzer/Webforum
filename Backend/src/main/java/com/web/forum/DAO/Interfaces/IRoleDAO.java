package com.web.forum.DAO.Interfaces;

import com.web.forum.Entity.Role;

public interface IRoleDAO {

    //Create and save new Role
    public String create(String roleName);

    //Find specific Role based on ID
    public Role read(int ID);

    //Find specific Role based on name
    public Role readByName(String name);

    //Delete existing Role
    public String delete(String roleName);
}
