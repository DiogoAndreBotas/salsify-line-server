package com.diogoandrebotas.salsifylineserver;

import java.util.NoSuchElementException;

import com.diogoandrebotas.salsifylineserver.controller.LinesController;
import com.diogoandrebotas.salsifylineserver.service.LinesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LinesControllerTest {

    @InjectMocks
    private LinesController linesController;

    @Mock
    private LinesService linesService;

    @Test
    @DisplayName("Get existing line returns 200 OK")
    void getLineSucceeds() {
        Mockito.when(linesService.getLine(100)).thenReturn("Line number 100");

        var lines = linesController.getLine(100);

        assertEquals("Line number 100", lines.getBody());
        assertEquals(HttpStatusCode.valueOf(200), lines.getStatusCode());
    }

    @Test
    @DisplayName("Get out of bounds line returns 413 Payload Too Large")
    void getLineOutOfBounds() {
        Mockito.when(linesService.getLine(100)).thenThrow(NoSuchElementException.class);

        var lines = linesController.getLine(100);

        assertEquals(HttpStatusCode.valueOf(413), lines.getStatusCode());
    }
}
