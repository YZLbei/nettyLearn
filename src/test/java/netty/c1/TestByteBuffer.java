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
        //��ȡFileChannel������1.���������2.RandomAccessFile�������ȡ��
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //׼��������
            //allocate�൱��ΪByteBuffer����ʮ���ֽڵĿռ���Ϊ������
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while(true){
                //��channel��ȡ���ݿ⣬��bufferд������
                int len = channel.read(buffer);
                log.debug("��ȡ�����ֽ���{}",len);
                if (len==-1){
                    break;
                }
                buffer.flip();//�л�����ģʽ
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    //System.out.println((char)b);
                    log.debug("ʵ�ʶ�ȡ�����ֽ�{}",(char)b);
                }
                buffer.clear();//�л�Ϊдģʽ
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
