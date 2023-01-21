package com.diogoandrebotas.salsifylineserver;

import java.util.NoSuchElementException;

import com.diogoandrebotas.salsifylineserver.service.LinesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LinesServiceTest {

    @Autowired
    private LinesService linesService;

    @Test
    @DisplayName("Read first line from file with five lines")
    void readFirstLineSmallFile() {
        String line = linesService.getLine("src/test/resources/file_with_five_lines.txt", 0);

        assertEquals("Once there was a big file", line);
    }

    @Test
    @DisplayName("Read last line from file with five lines")
    void readLastLineSmallFile() {
        String line = linesService.getLine("src/test/resources/file_with_five_lines.txt", 4);

        assertEquals("To be used by humans", line);
    }

    @Test
    @DisplayName("Read line beyond number of lines in file")
    void readBeyondLines() {
        assertThrows(
            NoSuchElementException.class,
            () -> linesService.getLine("src/test/resources/file_with_five_lines.txt", 100)
        );
    }

    @Test
    @DisplayName("Read last line from file with ten thousand lines")
    void readLastLineBigFile() {
        String line = linesService.getLine("src/test/resources/file_with_10k_lines.txt", 9999);

        assertEquals("In there stepped a stately Raven of the saintly days of yore;", line);
    }

    @Test
    @DisplayName("Read line from nonexistent file")
    void readNonexistentFile() {
        assertThrows(
            RuntimeException.class,
            () -> linesService.getLine("src/test/resources/limbo.txt", 10),
            "Failed to read file"
        );
    }

    @Test
    @DisplayName("Read line from empty file")
    void readEmptyFile() {
        assertThrows(
            NoSuchElementException.class,
            () -> linesService.getLine("src/test/resources/empty_file.txt", 0)
        );
    }
}
