package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP Status code
// 2xx -> OK
// 4xx -> Client
// 5xx -> Server
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {   // RuntimeException : 실행시 발생하는 오류
    public UserNotFoundException(String message) {
        super(message);
    }
}
