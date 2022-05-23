package netty.c1;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static netty.c1.ByteBufferUtil.debugAll;

public class Exercise {
    public static void main(String[] args) {
        String s1 = "Hello,world\nI'm zhangsan\nHo";
        String s2 = "w are you?\n";
        ByteBuffer buffer = ByteBuffer.allocate(30);
        buffer.put(s1.getBytes());
        split(buffer);
        buffer.put(s2.getBytes());
        split(buffer);
    }
    
    public static void split( ByteBuffer buffer){
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i)=='\n'){
                int length  = i-buffer.position()+1;
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }
                debugAll(target);
            }
        }
        buffer.compact();
    }
}
