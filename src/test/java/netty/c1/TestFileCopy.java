package netty.c1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileCopy {
    public static void main(String[] args) {
        String source  = "D:\\学习\\工作\\Netty网络编程";
        String target = "D:\\学习\\工作\\Netty网络编程aaa";
        try {
            Files.walk(Paths.get(source)).forEach(path -> {
                String targetName = path.toString().replace(source,target);
                //是文件夹
                try {
                    if (Files.isDirectory(path)){
                        Files.createDirectory(Paths.get(targetName));
                    }
                    else if (Files.isRegularFile(path)){
                        Files.copy(path, Paths.get(targetName));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
