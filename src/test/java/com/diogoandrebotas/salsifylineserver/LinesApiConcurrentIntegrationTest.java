package com.diogoandrebotas.salsifylineserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LinesApiConcurrentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void threadOne() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void threadTwo() throws Exception {
        var response = mockMvc.perform(get("/lines/10001"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Cras metus leo, lobortis vitae quam at, aliquet fermentum est."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void threadThree() throws Exception {
        var response = mockMvc.perform(get("/lines/10002"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Morbi finibus, nisi et varius ullamcorper, nibh dolor tempor ex, pulvinar vulputate magna erat a tellus."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void threadFour() throws Exception {
        var response = mockMvc.perform(get("/lines/10003"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("In egestas, neque et gravida consequat, sapien risus mattis purus, eget laoreet est nunc et elit."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void threadFive() throws Exception {
        var response = mockMvc.perform(get("/lines/10004"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Aenean malesuada sem lectus, sed laoreet nisi pretium sed."));
    }
}
