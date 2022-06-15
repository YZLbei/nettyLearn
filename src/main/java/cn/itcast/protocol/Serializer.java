package cn.itcast.protocol;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 20:10
 * @Description: ���л��ӿ�
 */
public interface Serializer {
    // �����л�����
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // ���л�����
    <T> byte[] serialize(T object);

    /**
     * ö�����Ƕ���
     */
    enum Algorithm implements Serializer {
        
        Java{
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("�����л�ʧ��");
                }

            }

            @Override
            public <T> byte[] serialize(T object) {
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    byte[] bytes = bos.toByteArray();
                    return bytes;
                } catch (IOException e) {
                    throw new RuntimeException("���л�ʧ��");
                }
            }
        },
        Json{
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                String s = new String(bytes,StandardCharsets.UTF_8);
                T obj = new Gson().fromJson(s, clazz);
                return obj;
            }

            @Override
            public <T> byte[] serialize(T object) {
                //��java����ת����Json���ַ���
                String s = new Gson().toJson(object);
                return s.getBytes(StandardCharsets.UTF_8);
            }
        }
    }
}
