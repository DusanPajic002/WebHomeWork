package http.response;

import java.net.HttpCookie;

public class QuoteResponse extends Response{

    private final String quote;

    public QuoteResponse(String quote) {
        this.quote = quote;
    }

    @Override
    public String getResponseString() {
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n";
        response += "\r\n";
        response += quote;


        return response;
    }
}
