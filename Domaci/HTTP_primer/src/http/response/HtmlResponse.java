package http.response;

import java.net.HttpCookie;

public class HtmlResponse extends Response {

    private final String html;

    public HtmlResponse(String html) {
        this.html = html;
    }

    @Override
    public String getResponseString() {
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n";
        int i = 0;
        for (HttpCookie cookie : getCookies()) {
            System.out.println(++i + "  - i");
            response += "Set-Cookie: " + cookie.toString() + "\r\n";
        }
        response += "\r\n";
        response += html;

        return response;
    }
}
