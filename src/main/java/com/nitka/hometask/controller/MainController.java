package com.nitka.hometask.controller;

import com.nitka.hometask.processing.ProcessingService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class MainController {

    private final ProcessingService processingService;


    public MainController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/process-file/{consumerQty}")
    public void processFile(@RequestPart("file") final MultipartFile file, @PathVariable Integer consumerQty) throws IOException {

        processingService.process(file.getInputStream(), file.getName(), consumerQty);

    }

}
