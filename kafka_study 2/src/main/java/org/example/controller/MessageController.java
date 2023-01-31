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
@RequestMapping("/messages")
public class MessageController {

    private final UserService userService;

    @PostMapping
    public int create(@RequestBody MessageRequest request) {
        UserSendMessagesEntity result = this.userService.sendMessage(request);



        return 1;
    }
}
