package com.example.demo.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-service-test-data.sql")
public class UserServiceTest {
}
