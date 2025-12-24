package com.web.forum.Repository.Interfaces;

import org.springframework.http.ResponseEntity;

import com.web.forum.Entity.Role;

public interface IRoleRepository {

    //Create and save new Role
    public ResponseEntity<String> save(String roleName);

    //Find specific Role based on ID
    public Role find(int ID);

    //Delete existing Role
    public ResponseEntity<String> remove(String roleName);
}
