package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.*;
import org.example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        log.info("CREATE");
        System.out.println(request.getName());
        if (ObjectUtils.isEmpty(request.getName()))
            return ResponseEntity.badRequest().build();

        System.out.println(request.getEmail());
        if (ObjectUtils.isEmpty(request.getEmail()))
            return ResponseEntity.badRequest().build();
        
        UserEntity result = this.userService.add(request);
        return ResponseEntity.ok(new UserResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> readAll() {
        log.info("READ ALL");
        List<UserEntity> result = this.userService.searchAll();
        List<UserResponse> response = result.stream().map(UserResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> readOne(@PathVariable Long id) {
        log.info("READ");
        UserEntity result = this.userService.searchById(id);
        return ResponseEntity.ok(new UserResponse(result));
    }
}
