package com.web.forum.DAO.Interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface IUserRoleDAO {

    //Create and save new UserRole
    public ResponseEntity<String> create(Long userID, int roleID);

    //Find all Roles of specific User by id
    public List<String> readById(Long userId);

    //Delete existing user role
    public ResponseEntity<String> delete(Long userID, int roleID);
}
