package com.github.br_dr3.freecell.gateway.controller;

import com.github.br_dr3.freecell.gateway.dto.RequestUserCreationDTO;
import com.github.br_dr3.freecell.gateway.dto.UserDTO;
import com.github.br_dr3.freecell.service.UsersService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    private ResponseEntity<DataWrapper<UserDTO>> createUser(@RequestBody RequestUserCreationDTO requestUserCreation) {
        var user = usersService.createUser(requestUserCreation);
        return ResponseEntity.status(HttpStatus.CREATED).body(DataWrapper.<UserDTO>builder().data(user).build());
    }

    @GetMapping("/{userId}")
    private ResponseEntity<DataWrapper<UserDTO>> getUser(@PathVariable("userId") Long userId) {
        var user = usersService.getUser(userId);
        return ResponseEntity.ok(DataWrapper.<UserDTO>builder().data(user).build());
    }
}
