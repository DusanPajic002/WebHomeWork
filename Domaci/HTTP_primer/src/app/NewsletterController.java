package app;

import http.Request;
import http.Server.Server;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;

public class NewsletterController extends Controller {
    public NewsletterController(Request request) {
        super(request);
    }
   @Override
   public Response doGet() {
       String htmlBody = "<h1>Quote Keeper</h1>" +
               "<form method=\"POST\" action=\"/savequote\">" +
               "<label for=\"author\">Author</label>" +
               "<input type=\"text\" id=\"author\" name=\"author\" placeholder=\"Author\"><br><br>" +
               "<label for=\"quote\">Quote</label>" +
               "<textarea id=\"quote\" name=\"quote\" placeholder=\"Quote\"></textarea><br><br>" +
               "<button>Save Quote</button>" +
               "</form>" +
               "<div>" +
               "<h2>Quote of the day:</h2>" +
               "<p>" + Server.quoteOfTheDay + "</p>" +
               "</div>" +
               "<div>" +
               "<h2>Saved Quotes</h2>" +
               "<ul>";


       for (int i=0; i<Server.savedQuotes.size(); i++) {
           String author = Server.savedQuotes.get(i).getAuthor().replace("+", " ");
           String quote = Server.savedQuotes.get(i).getQuote().replace("+", " ");
           htmlBody += "<li>" + String.format("%s: %s", author, quote) + "</li>";
       }

       htmlBody += "</ul></div>";

       return new HtmlResponse(htmlBody);
   }

    @Override
    public Response doPost() {
            return new RedirectResponse("/quotes");
    }
}
/*

 */