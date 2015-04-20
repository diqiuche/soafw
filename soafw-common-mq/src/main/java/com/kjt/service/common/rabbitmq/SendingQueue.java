package com.kjt.service.common.rabbitmq;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.kjt.service.common.mq.IMessage;

public class SendingQueue {
    public SendingQueue() {
        queue = new ConcurrentLinkedQueue<IMessage>();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean add(IMessage value) {
        return queue.add(value);
    }

    public IMessage poll() {
        return queue.poll();
    }

    private Queue<IMessage> queue;
}
