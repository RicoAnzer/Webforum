package com.web.forum.Repository.Interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface IUserRoleRepository {

    //Create and save new user role
    public ResponseEntity<String> save(Long userID, int roleID);

    //Find all Roles of specific User by id
    public List<String> findById(Long userId);
}
