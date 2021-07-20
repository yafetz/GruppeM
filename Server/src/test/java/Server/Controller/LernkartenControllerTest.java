package Server.Controller;

import Server.Modell.ChatRaum;
import Server.Modell.Freundschaft;
import Server.Modell.Gruppenmitglied;
import Server.Modell.Nutzer;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LernkartenControllerTest {


    @Autowired
    private MockMvc mockMvc;

    String content;
    String afterContent;
    int before;
    int after;

    @BeforeEach
    public void setUp() throws Exception{

        MvcResult result = this.mockMvc.perform(get("http://localhost:8080/lernkarten/getlernkarten/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        content = result.getResponse().getContentAsString();

    }



    @Test
    void test(){

        try {
            MvcResult result = this.mockMvc.perform(get("http://localhost:8080/lernkarten/getlernkarten/2")).andDo(print()).andReturn();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @AfterEach
    void alleNachrichten() throws Exception {

    }

}