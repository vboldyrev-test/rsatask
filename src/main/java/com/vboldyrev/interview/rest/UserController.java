package com.vboldyrev.interview.rest;

import com.vboldyrev.interview.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping("/users/{name}/last_access_date")
    public String getUserLastAccessDate(@PathVariable String name) {
        Optional<String> optionalDate = service.getUserDate(name);
        service.updateUserDate(name);
        if(!optionalDate.isPresent()) service.clearUserCacheByName(name);
        return optionalDate.orElse("");
    }
}
