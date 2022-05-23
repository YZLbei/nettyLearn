package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(new InetSocketAddress("localhost",8080));
            System.out.println("waiting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
