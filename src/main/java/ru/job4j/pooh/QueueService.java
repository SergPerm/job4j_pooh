package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> mapWithQueue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204");
        switch (req.httpRequestType()) {
            case "POST" -> {
                String sourceName = req.getSourceName();
                String text = req.getParam();
                mapWithQueue.putIfAbsent(sourceName, new ConcurrentLinkedQueue<>());
                mapWithQueue.get(sourceName).add(text);
                result = new Resp(text, "200");
            }
            case "GET" -> {
                ConcurrentLinkedQueue<String> queue = mapWithQueue.get(req.getSourceName());
                if (queue != null) {
                    String text = queue.poll();
                    if (text != null) {
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
