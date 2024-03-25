package http.SideServer;
import app.AuthorQuote;
import app.RequestHandler;
import com.google.gson.Gson;
import http.HttpMethod;
import http.Request;
import http.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SideServerThread implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public SideServerThread(Socket sock) {
        this.client = sock;

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);

            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();

            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));

            if (method.equals(HttpMethod.POST.toString())) {
                StringBuilder sb = new StringBuilder();
                while (in.ready())
                    sb.append((char) in.read());

                String s = sb.toString();
                s = s.replaceAll("\"", "");
                s = s.replaceAll("\\[", "");
                s = s.replaceAll("\\]", "");
                s = s.replaceAll("\\+", " ");
                List<String> matches = extractJsonObjects(s);
                for (String match : matches) {
                    String[] parts = match.split(",",2);
                    parts[0] = parts[0].replaceAll("\\{", "");
                    parts[1] = parts[1].replaceAll("}", "");
                    System.out.println(parts[0] + " - " + parts[1]);
                    SideServer.savedQuotes.add(new AuthorQuote(parts[0].substring(7,parts[0].length()), parts[1].substring(6,parts[1].length())));
                }
            }

            Request request = new Request(HttpMethod.valueOf(method), path);
            RequestHandler requestHandler = new RequestHandler();
            Response response = requestHandler.handle(request);


            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());

            out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> extractJsonObjects(String input) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }
}
