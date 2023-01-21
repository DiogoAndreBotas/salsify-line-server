package com.diogoandrebotas.salsifylineserver.controller;

import java.util.NoSuchElementException;

import com.diogoandrebotas.salsifylineserver.service.LinesService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinesController {

    private final LinesService linesService;

    public LinesController(LinesService linesService) {
        this.linesService = linesService;
    }

    @GetMapping("/lines/{lineIndex}")
    public ResponseEntity<String> getLine(@PathVariable long lineIndex) {
        try {
            var line = linesService.getLine(lineIndex);
            return new ResponseEntity<>(line, HttpStatusCode.valueOf(200));
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(413));
        }
    }

}
