package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")   // 도메인 공통
public class AdminUserController {
    private UserDaoService service; //의존성 주입 필요

    // 생성자를 통한 인스턴스 주입
    public AdminUserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = service.findAll();   // ctrl+alt+v

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);  // User에서 정의한 Filter

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    // Get /users/1 or /users/2 ==> id : String
    // Get /admin/users/1 ==> /admin/v1/users/1
    // @GetMapping("/v1/users/{id}")      // MappingJacksonValue : filter를 사용하기 위함
    //@GetMapping(value = "/users/{id}/", params = "version=1")
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")     // 마임타입 이용, 헤더값이용해 접속
    public MappingJacksonValue retrieveUserV1(@PathVariable("id") int id){
        User user = service.findOne(id);    // ctrl + alt + v

        if(user == null){
            throw new UserNotFoundException(String.format("Id[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "password", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);  // User에서 정의한 Filter

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    //@GetMapping("/v2/users/{id}")      // MappingJacksonValue : filter를 사용하기 위함
    //@GetMapping(value = "/users/{id}/", params = "version=2")
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable("id") int id){
        User user = service.findOne(id);    // ctrl + alt + v

        if(user == null){
            throw new UserNotFoundException(String.format("Id[%s] not found", id));
        }

        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);     // id, name, joinDate, password, ssn
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);  // User에서 정의한 Filter

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }

}
