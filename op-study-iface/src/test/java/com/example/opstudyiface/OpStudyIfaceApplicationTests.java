package com.example.opstudyiface;

import com.example.application.iface.model.UserRequest;
import com.example.iface.user.UserIFace;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpStudyIFaceApplicationTests {

    private UserIFace userIFace;

    @Test
    void contextLoads() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("test");
        userRequest.setMobilePhone("12345678901");
        userIFace.registerUser(userRequest);
    }

}
