package cn.itcast.protocol;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 20:10
 * @Description: 序列化接口
 */
public interface Serializer {
    // 反序列化方法
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // 序列化方法
    <T> byte[] serialize(T object);

    /**
     * 枚举中是对象
     */
    enum Algorithm implements Serializer {
        
        Java{
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败");
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
                    throw new RuntimeException("序列化失败");
                }
            }
        },
        Json{
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class,new Serializer.ClassCodec()).create();
                String s = new String(bytes,StandardCharsets.UTF_8);
                T obj = gson.fromJson(s, clazz);
                return obj;
            }

            @Override
            public <T> byte[] serialize(T object) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class,new Serializer.ClassCodec()).create();
                //把java对象转化成Json的字符串
                String s = gson.toJson(object);
                return s.getBytes(StandardCharsets.UTF_8);
            }
        }
    }
    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override             //   String.class
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            // class -> json
            return new JsonPrimitive(src.getName());
        }
    }
}
