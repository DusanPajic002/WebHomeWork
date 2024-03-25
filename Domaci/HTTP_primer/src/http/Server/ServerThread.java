package http.Server;

import app.RequestHandler;
import com.google.gson.Gson;
import http.HttpMethod;
import http.Request;
import http.response.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket sock) {
        this.client = sock;

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                System.out.println(s);

                String[] params = s.split("&");
                Server.savedQuotes.add(new app.AuthorQuote(params[0].split("=")[1], params[1].split("=")[1]));
                System.out.println(Server.savedQuotes);
            }

            Request request = new Request(HttpMethod.valueOf(method), path);

            if (Server.isRunning && request.getPath().equals("/quotes") && request.getHttpMethod().equals(HttpMethod.GET)) {
                Socket socket = new Socket("localhost", 8114);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                String quotesJson = new Gson().toJson(Server.savedQuotes);
                out.println("POST /quoteoftheday HTTP/1.1");
                out.println("Host: localhost:8114");
                out.println("Content-Length: " + quotesJson.length());
                out.println("Content-Type: application/json; charset=UTF-8");
                out.println("\n");
                out.println(quotesJson);
                out.println();

                String responseLine;
                while (!(responseLine = in.readLine()).equals("")) {
                    System.out.println(responseLine);
                }

                StringBuilder responseBody = new StringBuilder();
                while (in.ready()) {
                    responseBody.append((char) in.read());
                }

                String quoteOfTheDay = responseBody.toString();
                Server.quoteOfTheDay = quoteOfTheDay;

                in.close();
                out.close();
                socket.close();
                Server.isRunning = false;
            }
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
            e.printStackTrace();
        }
    }
}
