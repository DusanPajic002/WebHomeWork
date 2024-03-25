package http.response;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public abstract class Response {
    public List<HttpCookie> cookies = new ArrayList<>();
    public abstract String getResponseString();
    public void addCookie(HttpCookie cookie) {
        this.cookies.add(cookie);
    }
    public List<HttpCookie> getCookies() {
        return this.cookies;
    }
}
