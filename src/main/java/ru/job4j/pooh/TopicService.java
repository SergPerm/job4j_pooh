package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> storage =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204");
        switch (req.httpRequestType()) {
            case "POST" -> {
                String topicName = req.getSourceName();
                String text = req.getParam();
                var idInTopic = storage.get(topicName);
                if (idInTopic != null) {
                    for (ConcurrentLinkedQueue<String> id : idInTopic.values()) {
                        id.add(text);
                    }
                    result = new Resp(req.getParam(), "200");
                }
            }
            case "GET" -> {
                String topicName = req.getSourceName();
                String id = req.getParam();
                storage.putIfAbsent(topicName, new ConcurrentHashMap<>());
                storage.get(topicName).putIfAbsent(id, new ConcurrentLinkedQueue<>());
                String text = storage.get(topicName).get(id).poll();
                if (text != null) {
                    result = new Resp(text, "200");
                }
            }
            default -> {
                result  = new Resp("Not implemented", "400");
            }
        }
        return result;
    }
}
