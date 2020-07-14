package com.example.restfulwebservice.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service; //의존성 주입 필요

    // 생성자를 통한 인스턴스 주입
    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    // Get /users/1 or /users/2 ==> id : String
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable("id") int id){
        User user = service.findOne(id);    // ctrl + alt + v

        if(user == null){
            throw new UserNotFoundException(String.format("Id[%s] not found", id));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") int id){
        User user = service.deleteByid(id);

        if(user == null){
            throw new UserNotFoundException(String.format("Id[%s] not found", id));
        }
    }
}