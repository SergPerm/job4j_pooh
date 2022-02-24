package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> mapWithQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result  = new Resp("Bad request", "400");
        if ("POST".equals(req.httpRequestType())) {
            String sourceName = req.getSourceName();
            String text = req.getParam();
            ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
            queue.add(text);
            queue = mapWithQueue.putIfAbsent(sourceName, queue);
            if (queue != null) {
                queue.add(text);
            }
            result = new Resp("Post added", "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> queue = mapWithQueue.get(req.getSourceName());
            if (queue == null) {
                result = new Resp("Empty queue", "204");
            } else {
                String text = queue.poll();
                result = new Resp(text, "200");
            }
        }
        return result;
    }
}
