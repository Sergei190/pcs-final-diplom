import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;

public class Main {
    static final int SERVER_PORT = 8989;

    public static void main(String[] args) throws Exception {
        SearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Запуск сервера по адресу: " + SERVER_PORT + "...");

            while (true) {
                try (
                        var socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    // Принимаем запрос
                    var request = in.readLine();

                    // Запрашиваем результат и отправляем его клиенту
                    var response = gson.toJson(engine.search(request));
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не запускается сервер!");
            e.printStackTrace();
        }
    }
}