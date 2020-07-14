package com.example.restfulwebservice.helloworld;
// lombok

import lombok.AllArgsConstructor;
import lombok.Data;

@Data   // Sette Getter 생성자 생성됨.
@AllArgsConstructor // 생성자
public class HelloWorldBean {
    private String message;

}
