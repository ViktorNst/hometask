package com.nitka.hometask.service;

import com.nitka.hometask.processing.ProcessingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ProcessingServiceTest {

    ProcessingService processingService = new ProcessingService();

    @Test
    public void smokeTest() throws InterruptedException {
        String data = "B|0|B-00\n" +
                "A|0|A-00\n" +
                "C|0|C-00\n" +
                "C|0|C-01\n" +
                "C|0|C-02\n" +
                "A|0|A-01\n" +
                "B|0|B-01\n" +
                "A|0|A-02";

        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        processingService.process(inputStream, "test.txt", 3);
        Thread.sleep(50);
    }
}