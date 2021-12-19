package com.example.demo.core;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内存队列
 */
public class MemoryQueue {

    private ArrayList<KmqMessage> queue = new ArrayList<>();

    private AtomicInteger index = new AtomicInteger(0);

    public boolean offer(KmqMessage message) {
        synchronized (this.queue) {
            queue.add(message);
            return true;
        }
    }

    public KmqMessage poll() {
        // 当前未读取过的message数量
        int currentMessages = queue.size() - index.get() - 1;

        KmqMessage message = null;
        synchronized (this.queue) {
            int beginIndex = index.get();
            if (currentMessages > 0) {
                message = queue.get(beginIndex);
                index.set(beginIndex + 1);
            }
        }
        return message;
    }
}
