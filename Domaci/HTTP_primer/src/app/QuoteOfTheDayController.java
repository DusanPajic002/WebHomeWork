package app;

import http.Request;
import http.SideServer.SideServer;
import http.response.QuoteResponse;
import http.response.RedirectResponse;
import http.response.Response;

import java.net.HttpCookie;

public class QuoteOfTheDayController extends Controller{
    public QuoteOfTheDayController(Request request) {
        super(request);
    }
    private String quoteOfTheDay;
    @Override
    public Response doGet() {
        return null;
    }

    @Override
    public Response doPost() {

        int randomIndex = (int) (Math.random() * SideServer.savedQuotes.size());
        AuthorQuote randomQuote = SideServer.savedQuotes.get(randomIndex);
        quoteOfTheDay = randomQuote.toString();

        HttpCookie cookie = new HttpCookie("DailyQuote", quoteOfTheDay);
        cookie.setMaxAge(24 * 60 * 60);

        QuoteResponse response = new QuoteResponse(quoteOfTheDay);
        boolean b = false;
        for (HttpCookie c : response.getCookies())
            if (c.getName().equals("DailyQuote"))
                b = true;
        if (!b)
            response.addCookie(cookie);

        return response;
        }
}
