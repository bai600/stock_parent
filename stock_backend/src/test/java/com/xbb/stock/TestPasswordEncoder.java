package com.xbb.stock;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.PublicKey;


@SpringBootTest
public class TestPasswordEncoder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test01(){
        String pwd="123456";
        String encodePwd=passwordEncoder.encode(pwd);
        System.out.println(encodePwd);
    }

    @Test
    public void test02(){
        String pwd="123456";
        String encodePwd="$2a$10$cwtSqoNmu1USj1UeF34/U.X13S6ZIXnP5txA59bFaQAgrYhX/dLOO";
        boolean isSucess = passwordEncoder.matches(pwd,encodePwd);
        System.out.println(isSucess?"密码匹配成功":"密码匹配失败");
    }
}
