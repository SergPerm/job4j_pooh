package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> storage =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result;
        switch (req.httpRequestType()) {
            case "POST" -> {
                String topicName = req.getSourceName();
                String text = req.getParam();
                var idInTopic = storage.get(topicName);
                result = new Resp("", "204");
                if (idInTopic != null) {
                    for (Map.Entry<String, ConcurrentLinkedQueue<String>> id : idInTopic.entrySet()) {
                        id.getValue().add(text);
                    }
                    result = new Resp("", "200");
                }
            }
            case "GET" -> {
                String topicName = req.getSourceName();
                String id = req.getParam();
                storage.putIfAbsent(topicName, new ConcurrentHashMap<>());
                storage.get(topicName).putIfAbsent(id, new ConcurrentLinkedQueue<>());
                String text = storage.get(topicName).get(id).poll();
                if (text == null) {
                    result = new Resp("", "204");
                } else {
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
