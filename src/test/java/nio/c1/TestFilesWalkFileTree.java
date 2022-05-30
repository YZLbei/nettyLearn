package nio.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFilesWalkFileTree {
    public static void main(String[] args) {
        AtomicInteger dirNum = new AtomicInteger();
        AtomicInteger fileNum = new AtomicInteger();
        try {
            Files.walkFileTree(Paths.get("D:\\Ñ§Ï°\\¹¤×÷"),new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("=====>"+dir);
                    dirNum.incrementAndGet();
                    return super.preVisitDirectory(dir, attrs);
                }
    
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    fileNum.incrementAndGet();
                    return super.visitFile(file, attrs);
                }
            });
            System.out.println("dir"+dirNum);
            System.out.println("file"+fileNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
