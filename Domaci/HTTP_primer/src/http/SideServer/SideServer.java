package http.SideServer;

import app.AuthorQuote;
import http.Server.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SideServer {

    public static final int TCP_PORT = 8114;
    public static List<AuthorQuote> savedQuotes = new ArrayList<>();

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new SideServerThread(sock)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
