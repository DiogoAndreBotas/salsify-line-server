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
    void success() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void success2() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void success3() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void success4() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void success5() throws Exception {
        var response = mockMvc.perform(get("/lines/10000"));

        response
            .andExpect(status().isOk())
            .andExpect(content().string("Sed eleifend nulla vitae justo varius, eu congue nulla pellentesque."));
    }
}
