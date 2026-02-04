package com.web.forum.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.DAO.RoleDAO;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleDAO roleDAO;

    //Add new Role
    @PostMapping("/add/{roleName}")
    public ResponseEntity<?> addRole(@PathVariable String roleName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDAO.create(roleName));
    }

    //Delete existing Role
    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<?> deleteRole(@PathVariable String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(roleDAO.delete(roleName));
    }
}
