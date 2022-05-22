package netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        //FileChannel 
        //获取FileChannel有两种1.输入输出流2.RandomAccessFile（随机读取）
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区
            //allocate相当于为ByteBuffer划分十个字节的空间作为缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while(true){
                //从channel读取数据库，向buffer写入数据
                int len = channel.read(buffer);
                log.debug("读取到的字节数{}",len);
                if (len==-1){
                    break;
                }
                buffer.flip();//切换至读模式
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    //System.out.println((char)b);
                    log.debug("实际读取到的字节{}",(char)b);
                }
                buffer.clear();//切换为写模式
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
