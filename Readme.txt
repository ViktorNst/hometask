Message processing algorithm:
The processing service creates new instance of Message processor(MP) for each file.
MP reads each message, creates new processing task and stores it in the queue.
MP submits task to executor if the queue is empty.
When the task is completed, MP removes it from queue, takes next task from the queue and submits it to executor.
There is a separate queue for each message ID. All queues stored in hashmap.

