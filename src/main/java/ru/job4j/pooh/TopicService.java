package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> storage =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result  = new Resp("Bad request in program", "400");
        if ("POST".equals(req.httpRequestType())) {
            String topicName = req.getSourceName();
            String text = req.getParam();
            var idInTopic = storage.get(topicName);
            if (idInTopic == null) {
                idInTopic = new ConcurrentHashMap<>();
                storage.putIfAbsent(topicName, idInTopic);
            }
            for (Map.Entry<String, ConcurrentLinkedQueue<String>> id : idInTopic.entrySet()) {
                id.getValue().add(text);
            }
            result = new Resp("Post added", "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            String topicName = req.getSourceName();
            String id = req.getParam();
            var idInTopic = storage.get(topicName);
            if (idInTopic == null) {
                idInTopic = new ConcurrentHashMap<>();
                storage.putIfAbsent(topicName, idInTopic);
            }
            ConcurrentLinkedQueue<String> queue = idInTopic.get(id);
            if (queue == null) {
                idInTopic.put(id, new ConcurrentLinkedQueue<>());
                result = new Resp("", "204");
            } else {
                String text = queue.poll();
                result = new Resp(text, "200");
            }
        }
        return result;
    }
}
