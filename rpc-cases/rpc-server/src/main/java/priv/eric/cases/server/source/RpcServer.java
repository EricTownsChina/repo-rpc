package priv.eric.cases.server.source;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class RpcServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            while (true) {
                Socket socket = serverSocket.accept();
                Executors.newCachedThreadPool().execute(() -> {
                    try {
                        socketHandler(socket);
                    } catch (IOException e) {
                        // ignore
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void socketHandler(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        byte[] buff = new byte[1024];
        int index;
        while((index = bufferedInputStream.read(buff)) != -1) {
            String s = new String(buff, 0, index);
            printStream.printf("服务端收到: %s", s);
        }
    }

}
