package com.web.forum.DAO.Interfaces;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.Role;

public interface IRoleDAO {

    //Create and save new Role
    public ResponseEntity<String> create(String roleName);

    //Find specific Role based on ID
    public Role read(int ID);

    //Delete existing Role
    public ResponseEntity<String> delete(String roleName);
}
