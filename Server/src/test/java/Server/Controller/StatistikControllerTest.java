package Server.Controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class StatistikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    String content;
    String afterContent;

    @BeforeEach
    public void setUp(){

    }
    @Test
    void test(){

    }

    @AfterEach
    public void after(){

    }
}
