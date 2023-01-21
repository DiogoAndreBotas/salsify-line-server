package com.diogoandrebotas.salsifylineserver.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class LinesService {

    public String getLine(String filePath, long lineIndex) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.skip(lineIndex).findFirst().orElseThrow();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read file");
        }
    }

    public String getLine(long lineIndex) {
        return getLine("src/main/resources/file.txt", lineIndex);
    }

}
