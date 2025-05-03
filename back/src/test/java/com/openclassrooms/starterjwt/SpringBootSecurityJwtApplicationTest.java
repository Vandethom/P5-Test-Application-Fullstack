package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTest {

    @Test
    public void contextLoads() {
        // Testing that the Spring context loads successfully
    }

    @Test
    public void applicationStarts() {
        SpringBootSecurityJwtApplication.main(new String[]{});
    }
}