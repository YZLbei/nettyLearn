package netty.c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static netty.c1.ByteBufferUtil.debugAll;

public class TestByteBufferString {
    public static void main(String[] args) {
        //1.字符串转换为ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        debugAll(buffer1);
        
        
        //2.使用charest,会自动切换到读模式
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);
        
        //3.wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        String s = StandardCharsets.UTF_8.decode(buffer3).toString();
        System.out.println(s);
    }
}
