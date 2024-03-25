package http.Server;

import app.AuthorQuote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    public static final int TCP_PORT = 8113;
    public static List<AuthorQuote> savedQuotes = new ArrayList<>();
    public static String quoteOfTheDay = " ";
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static boolean isRunning = true;

    public static void main(String[] args) {
        Runnable task = () -> {
            isRunning = true;
        };
        executor.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        savedQuotes.add(new AuthorQuote("Alan Kay", "The best way to predict the future is to invent it."));
        savedQuotes.add(new AuthorQuote("AAyry", "Simple things should be simple, complex things should be possible."));
        savedQuotes.add(new AuthorQuote("Ada Lovelace", "The analytical engine has no pretensions to originate anything. It can do whatever we know how to order it to perform."));
        savedQuotes.add(new AuthorQuote("Grace Hopper", "The most dangerous phrase in the language is, We ve always done it this way."));
        savedQuotes.add(new AuthorQuote("Edsger Dijkstra", "Simplicity is prerequisite for reliability."));

        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new ServerThread(sock)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
