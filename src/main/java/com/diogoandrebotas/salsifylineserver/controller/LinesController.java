package com.diogoandrebotas.salsifylineserver.controller;

import java.util.NoSuchElementException;

import com.diogoandrebotas.salsifylineserver.service.LinesService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> getLine(@PathVariable String lineIndex) {
        try {
            var line = linesService.getLine(lineIndex);
            return new ResponseEntity<>(line, HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        } catch (NumberFormatException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
