package ru.job4j.pooh;

public class Trane {
    public static void main(String[] args) {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        System.out.println(content);
        System.out.println();
        String[] strings = content.split(ls);
        System.out.println(strings.length);
        System.out.println();
        System.out.println(strings[strings.length - 1]);
        System.out.println();
        String httpRequestType = content.split("/")[2].split(" ")[0];
        System.out.println(httpRequestType);

    }
}
