package Server.Controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatistikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    String content;
    String afterContent;

    @BeforeEach
    public void setUp() throws UnsupportedEncodingException {
        MvcResult result = null;
        try {
            result = this.mockMvc.perform(get("http://localhost:8080/review/teilnehmerAlle/frageId=4")).andDo(print()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        content = result.getResponse().getContentAsString();


    }
    @Test
    void test(){

        try {
            MvcResult result = this.mockMvc.perform(get("http://localhost:8080/review/teilnehmerBestanden/frageId=4")).andDo(print()).andReturn();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @AfterEach
    public void after(){

    }
}
