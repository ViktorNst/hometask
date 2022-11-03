package com.nitka.hometask.processing;

import com.nitka.hometask.logging.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;

@Service
public class ProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessingService.class);

    public void process(InputStream inputStream, String filename, int consumerQty) {
        MessageProcessor messageProcessor = new MessageProcessor(consumerQty);

        logger.info("PID: {}; START: {};  Consumers: {}; File: {}",
                Thread.currentThread().getId(), LogHelper.formatTime(Instant.now()), consumerQty,filename);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messageProcessor.process(line);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
