package Server.Controller;

import Server.Modell.Gruppenmitglied;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ChatController chatController;

    @Autowired
    private Gruppenmitglieder gruppenmitglieder;



    @BeforeTestClass
    public void setUp() throws Exception{
        this.mockMvc.perform(get("http://localhost:8080/chat/alleNachrichten/1")).andDo(print()).andExpect(status().isOk());

    }



    @Test
    void neueNachricht() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        this.mockMvc.perform(post("http://localhost:8080/chat/neueNachricht").param("chat_id", "1").param("nachricht", "Test Nachricht").param("datum",dtf.format(now)).param("nutzer_id","1")).andDo(print()).andExpect(status().isOk()).andExpect(content().string("OK"));

    }

    @AfterTestClass
    void afterrun(){

    }

    @Test
    void alleNachrichten() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/chat/alleNachrichten/1")).andDo(print()).andExpect(status().isOk());

    }





}