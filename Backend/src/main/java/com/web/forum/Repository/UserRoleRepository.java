package com.web.forum.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.web.forum.DAO.UserRoleDAO;
import com.web.forum.Repository.Interfaces.IUserRoleRepository;

@Repository
public class UserRoleRepository implements IUserRoleRepository {

    @Autowired
    private UserRoleDAO userRoleDAO;

    //Create and save new user role
    @Override
    public ResponseEntity<String> save(Long userID, int roleID) {
        return userRoleDAO.create(userID, roleID);
    }

    //Find all Roles of specific User by id
    @Override
    public List<String> findById(Long userId) {
        return userRoleDAO.readById(userId);
    }
}
