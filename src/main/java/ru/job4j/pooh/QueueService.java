package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> mapWithQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result;
        switch (req.httpRequestType()) {
            case "POST" -> {
                String sourceName = req.getSourceName();
                String text = req.getParam();
                ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
                queue.add(text);
                queue = mapWithQueue.putIfAbsent(sourceName, queue);
                if (queue != null) {
                    queue.add(text);
                }
                result = new Resp("", "200");
            }
            case "GET" -> {
                ConcurrentLinkedQueue<String> queue = mapWithQueue.get(req.getSourceName());
                if (queue == null) {
                    result = new Resp("", "204");
                } else {
                    String text = queue.poll();
                    if (text == null) {
                        result = new Resp("", "204");
                    } else {
                        result = new Resp(text, "200");
                    }
                }
            }
            default -> {
                result = new Resp("Not implemented", "400");
            }
        }
        return result;
    }
}
