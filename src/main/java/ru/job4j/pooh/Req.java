package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String ls = System.lineSeparator();
        String[] strings = content.split(ls);
        String[] parametrs = strings[0].split(" ");
        String[] types = parametrs[1].split("/");
        String httpRequestType = parametrs[0];
        String poohMode = types[1];
        String sourceName = types[2];
        String param = "";
        if (types.length > 3) {
            param = types[3];
        }
        if (strings[strings.length - 1].contains("=")) {
            param = strings[strings.length - 1];
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}