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
import org.springframework.http.HttpStatus;

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
        Mockito.when(linesService.getLine("100"))
            .thenReturn("Line number 100");

        var lines = linesController.getLine("100");

        assertEquals("Line number 100", lines.getBody());
        assertEquals(HttpStatus.OK, lines.getStatusCode());
    }

    @Test
    @DisplayName("Get out of bounds line returns 413 Payload Too Large")
    void getLineOutOfBounds() {
        Mockito.when(linesService.getLine("100"))
            .thenThrow(NoSuchElementException.class);

        var lines = linesController.getLine("100");

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, lines.getStatusCode());
    }

    @Test
    @DisplayName("Get line that results in Long overflow returns 400 Bad Request")
    void getLineOverflow() {
        Mockito.when(linesService.getLine("10000000000000000000"))
            .thenThrow(NumberFormatException.class);

        var lines = linesController.getLine("10000000000000000000");

        assertEquals(HttpStatus.BAD_REQUEST, lines.getStatusCode());
    }
}
