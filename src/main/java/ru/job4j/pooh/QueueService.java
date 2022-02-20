package ru.job4j.pooh;

public class QueueService implements Service {

    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            return new Resp("null", "204");
        }
        if ("GET".equals(req.httpRequestType())) {
            return new Resp("res", "200");
        }
        return null;
    }
}
