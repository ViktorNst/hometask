package com.nitka.hometask.processing;

import com.nitka.hometask.logging.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Reads messages and supplies them to executor in the right order
 */
public class MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private final long producerThreadId;
    private final ExecutorService executor;
    private final HashMap<String, LinkedList<Runnable>> idMap = new HashMap<>();

    public MessageProcessor(Integer consumerQty) {
        executor = Executors.newFixedThreadPool(consumerQty);
        producerThreadId = Thread.currentThread().getId();
    }

    public void process(String message) throws InterruptedException {
        final Instant messageReceivedAt = Instant.now();
        final String[] split = message.split("\\|");

        final int processingTime = Integer.parseInt(split[1]);
        final String id = split[0];
        final String content = split[2];

        if (id.isEmpty()) {
            Thread.sleep(processingTime);
        } else {
            final LinkedList<Runnable> queue = idMap.computeIfAbsent(id, k -> new LinkedList<>());

            Runnable callableTask = () -> {
                final Instant processingStart = Instant.now();
                try {
                    Thread.sleep(processingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                final Instant processingEnd = Instant.now();
                logger.info("PID: {};  {};  CID: {};  Start: {};  End: {};  Wait Time (ms): {}",
                        producerThreadId, content, Thread.currentThread().getId(),
                        LogHelper.formatTime(processingStart), LogHelper.formatTime(processingEnd),
                        processingStart.toEpochMilli() - messageReceivedAt.toEpochMilli());
                taskComplete(id);
            };

            synchronized (queue) {
                if (queue.size() == 0) {
                    queue.add(callableTask);
                    executor.submit(callableTask);
                } else {
                    queue.add(callableTask);
                }
            }

        }
    }

    private void taskComplete(String id) {
        final LinkedList<Runnable> queue = idMap.get(id);
        synchronized (queue) {
            queue.poll();
            if (queue.size() > 0) {
                executor.submit(queue.peek());
            }
        }
    }

}
