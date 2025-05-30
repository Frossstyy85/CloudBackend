package com.example.cloudbackend;



import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CloudBackendApplicationTests {

    @Test
    void contextLoads() {
        assertEquals(5 + 5, 10);
    }

}
