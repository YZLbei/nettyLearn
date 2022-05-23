package netty.c1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileCopy {
    public static void main(String[] args) {
        String source  = "D:\\ѧϰ\\����\\Netty������";
        String target = "D:\\ѧϰ\\����\\Netty������aaa";
        try {
            Files.walk(Paths.get(source)).forEach(path -> {
                String targetName = path.toString().replace(source,target);
                //���ļ���
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
