package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(new InetSocketAddress("localhost",8080));
//            System.out.println("waiting");
            SocketAddress address = socket.getLocalAddress();
            socket.write(Charset.defaultCharset().encode("helloworld122313123132131\n"));
            System.in.read();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
