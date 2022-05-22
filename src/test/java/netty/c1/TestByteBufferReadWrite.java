package netty.c1;

import java.nio.ByteBuffer;

import static netty.c1.ByteBufferUtil.debugAll;

public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        buffer.put((byte)0x61);//a
        debugAll(buffer);
        
        buffer.put(new byte[]{0x62,0x63});
        debugAll(buffer);
        
        buffer.flip();
        debugAll(buffer);
        
        buffer.get();
        debugAll(buffer);
        
        buffer.compact();
        debugAll(buffer);
    }
}
