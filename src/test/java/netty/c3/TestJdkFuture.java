package netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.�̳߳�
        ExecutorService executor = Executors.newFixedThreadPool(2);

        //2.�ύ����
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("ִ�м���");
                sleep(1000);
                return 20;
            }
        });
        
        //3.���߳�ͨ��future��ȡ���
        log.debug("�ȴ����");
        log.debug("{}",future.get());
        //future.get();

    }
}
