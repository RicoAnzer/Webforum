package com.web.forum.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.forum.Service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    //Add new Role
    @PostMapping("/{roleName}")
    public ResponseEntity<?> addRole(@PathVariable String roleName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleName));
    }

    //Delete existing Role
    @DeleteMapping("/{roleName}")
    public ResponseEntity<?> deleteRole(@PathVariable String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.deleteRole(roleName));
    }
}
